package ru.liga.loading.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.liga.loading.enums.LoadingMode;
import ru.liga.loading.models.Parcel;
import ru.liga.loading.models.Truck;
import ru.liga.loading.readers.TruckJsonReader;
import ru.liga.loading.services.LoadingService;
import ru.liga.loading.services.LoadingServiceFactory;
import ru.liga.loading.services.ParcelService;
import ru.liga.loading.services.TruckService;
import ru.liga.loading.services.UniformLoadingService;
import ru.liga.loading.utils.LoadingUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@ShellComponent
public class LoadingShellController {

    private final TruckJsonReader truckJsonReader;
    private final LoadingServiceFactory loadingServiceFactory;
    private final TruckService truckService;
    private final ParcelService parcelService;


    /** Эндпоинт для погрузки посылок в грузовики */
    @ShellMethod(key = "load")
    public String loadParcels(
            String mode,
            @ShellOption(defaultValue = "-1") int trucks,
            @ShellOption(defaultValue = "6") int height,
            @ShellOption(defaultValue = "6") int width,
            @ShellOption(defaultValue = "") String file,
            @ShellOption(defaultValue = "") String[] parcels
            ) {
        log.debug("Method '{}' has started", "loadParcels");

        if (!file.isBlank() && parcels.length > 0 || file.isBlank() && parcels.length == 0)
            throw new UnsupportedOperationException("You must enter no more and no less than " +
                    "1 parcels source (file/names)");

        List<Parcel> parcelList = file.isBlank() ? parcelService.getParcelsByNames(parcels)
                : parcelService.readParcelsFromFile(file);

        LoadingMode loadingMode = LoadingMode.valueOf(mode.toUpperCase());
        LoadingService loadingService = loadingServiceFactory.createLoadingServiceFromMode(loadingMode);

        List<Truck> loadedTrucks = loadTrucks(loadingService, parcelList, trucks, width, height);

        log.debug("Method '{}' has finished", "loadParcels");
        return truckService.getPrettyOutputForTrucks(loadedTrucks);
    }

    /** Эндпоинт для подсчёта посылок в грузовиках из json файла */
    @ShellMethod(key = "specify")
    public String specifyParcels(String file) {
            return truckService.getPrettyOutputForTrucks(truckJsonReader.readTrucksFromJson(file));
    }

    /**
     * Метод для отображения посылок (определённую посылку, если имя указано, все посылки - в противном случае).
     * @param name имя посылки (необязательно).
     * @throws FileNotFoundException если файл с посылками не найден.
     */
    @ShellMethod(key = "parcels")
    public String displayParcels(@ShellOption(defaultValue = "") String name) throws FileNotFoundException {
        return name.isBlank() ? parcelService.getPrettyOutputForAllParcels()
                : parcelService.getPrettyOutputForParcelByName(name);
    }

    /**
     * Эндпоинт для создания новой посылки.
     * @param name имя посылки.
     * @param symbol символ формы.
     * @throws IOException если произошла ошибка чтения формы посылки.
     */
    @ShellMethod(key = "create")
    public void createParcel(String name, char symbol) throws IOException {
        parcelService.save(name, symbol);
    }

    @ShellMethod(key = "update")
    public void updateParcel(
            String name,
            @ShellOption(defaultValue = "") String newName,
            @ShellOption(defaultValue = " ") char newSymbol
    ) throws IOException {
        parcelService.update(name, newName, newSymbol);
    }

    @ShellMethod(key = "delete")
    public void deleteParcel(String name) throws IOException {
        parcelService.delete(name);
    }

    private List<Truck> loadTrucks(
            LoadingService loadingService,
            List<Parcel> parcels,
            int trucks,
            int width,
            int height
    ) {
        log.info("Loading process has started");
        List<Truck> loadedTrucks;
        if (loadingService instanceof UniformLoadingService ) {
            List<Truck> emptyTrucks = LoadingUtils.generateEmptyTrucks(trucks, width, height);
            loadedTrucks = loadingService.loadTrucksWithParcelsWithGivenTrucks(parcels, new ArrayList<>(emptyTrucks));
        } else {
            loadedTrucks = loadingService.loadTrucksWithParcelsWithInfiniteTrucksAmount(parcels, width, height);
        }
        log.info("Loading has finished");
        return loadedTrucks;
    }
}
