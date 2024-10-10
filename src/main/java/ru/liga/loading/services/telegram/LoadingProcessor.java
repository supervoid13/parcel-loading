package ru.liga.loading.services.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import ru.liga.loading.enums.LoadingMode;
import ru.liga.loading.models.Parcel;
import ru.liga.loading.models.Truck;
import ru.liga.loading.readers.ParcelReader;
import ru.liga.loading.readers.TelegramFileDownloader;
import ru.liga.loading.services.LoadingService;
import ru.liga.loading.services.LoadingServiceFactory;
import ru.liga.loading.services.ParcelService;
import ru.liga.loading.services.TruckService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service("load")
@RequiredArgsConstructor
public class LoadingProcessor implements CommandProcessor {

    private static final int LOADING_MODE_INDEX = 1;
    private static final int AMOUNT_OF_TRUCKS_INDEX = 2;
    private static final int TRUCK_HEIGHT_INDEX = 3;
    private static final int TRUCK_WIDTH_INDEX = 4;
    private static final int PARCEL_LIST_FROM_INDEX = 5;

    private final TelegramFileDownloader telegramFileDownloader;
    private final LoadingServiceFactory loadingServiceFactory;
    private final TruckService truckService;
    private final ParcelReader parcelReader;
    private final ParcelService parcelService;

    @Override
    public String process(Message message) {
        String text = message.hasDocument() ? message.getCaption() : message.getText();
        String[] words = text.split("\\s");
        String mode = words[LOADING_MODE_INDEX];
        LoadingMode loadingMode = LoadingMode.valueOf(mode.toUpperCase());

        List<Parcel> parcels = getParcelsFromMessage(message);

        LoadingService loadingService = loadingServiceFactory.createLoadingServiceFromMode(loadingMode);

        int trucksAmount = Integer.parseInt(words[AMOUNT_OF_TRUCKS_INDEX]);
        int truckHeight = Integer.parseInt(words[TRUCK_HEIGHT_INDEX]), truckWidth = Integer.parseInt(words[TRUCK_WIDTH_INDEX]);

        List<Truck> loadedTrucks = loadingService.loadTrucks(parcels, trucksAmount, truckWidth, truckHeight);

        return truckService.getPrettyOutputForTrucks(loadedTrucks);
    }

    private List<Parcel> getParcelsFromMessage(Message message) {
        List<Parcel> parcels;
        if (message.hasDocument()) {
            String filePath;
            try {
                filePath = telegramFileDownloader.download(message.getDocument());
            } catch (Exception e) {
                return Collections.emptyList();
            }
            parcels = parcelReader.readParcelsFromFile(filePath);
        } else {
            String[] words = message.getText().split("\\s");
            List<String> parcelNames = getParcelNamesFromWords(words);
            parcels = parcelService.getParcelsByNames(parcelNames);
        }
        return parcels;
    }

    private List<String> getParcelNamesFromWords(String[] words) {
        return Arrays.asList(Arrays.copyOfRange(words, PARCEL_LIST_FROM_INDEX, words.length));
    }
}
