package ru.liga.loading.services.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import ru.liga.loading.services.ParcelService;

import java.util.NoSuchElementException;

@Service("parcels")
@RequiredArgsConstructor
public class RetrievingParcelsProcessor implements CommandProcessor {

    private static final int PARCEL_NAME_INDEX = 1;

    private final ParcelService parcelService;

    @Override
    public String process(Message message) {
        String[] words = message.getText().split("\\s");
        try {
            if (words.length == 1) {
                return parcelService.getPrettyOutputForAllParcels();
            }
            String name = words[PARCEL_NAME_INDEX];
            return parcelService.getPrettyOutputForParcelByName(name);
        } catch (NoSuchElementException e) {
            return e.getMessage();
        }
    }
}
