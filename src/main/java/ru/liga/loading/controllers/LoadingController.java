package ru.liga.loading.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.liga.loading.enums.LoadingMode;
import ru.liga.loading.models.Parcel;
import ru.liga.loading.models.Truck;
import ru.liga.loading.readers.ParcelReader;
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
public class LoadingController {

    private final TruckJsonReader truckJsonReader;
    private final ParcelReader parcelReader;
    private final LoadingServiceFactory loadingServiceFactory;
    private final TruckService truckService;
    private final ParcelService parcelService;


    /** Эндпоинт для погрузки посылок в грузовики */
    @ShellMethod(key = "load")
    public void loadParcels(
            String file,
            String mode,
            @ShellOption(defaultValue = "-1") int trucks,
            @ShellOption(defaultValue = "6") int height,
            @ShellOption(defaultValue = "6") int width
    ) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.debug("Method '%s' has started".formatted(methodName));
        try {
            List<Parcel> parcels = parcelReader.readParcelsFromFile(file);
            LoadingMode loadingMode = LoadingMode.valueOf(mode.toUpperCase());
            LoadingService loadingService = loadingServiceFactory.createLoadingServiceFromMode(loadingMode);

            List<Truck> loadedTrucks = loadTrucks(loadingService, parcels, trucks, width, height);
            truckService.displayTrucks(loadedTrucks);
        } catch (IOException e) {
            System.out.println("No such file");
            log.error("File not found");
        }
        log.debug("Method '%s' has finished".formatted(methodName));
    }

    /** Эндпоинт для подсчёта посылок в грузовиках из json файла */
    @ShellMethod(key = "specify")
    public void specifyParcels(String file) {
        try {
            List<Truck> trucks = truckJsonReader.readTrucksFromJson(file);
            truckService.displayParcelAmountAndTruckBodies(trucks);
        } catch (IOException e) {
            System.out.println("No such file");
            log.error("File not found");
        }
    }

    /**
     * Метод для отображения посылок (определённую посылку, если имя указано, все посылки - в противном случае).
     * @param name имя посылки (необязательно).
     * @throws FileNotFoundException если файл с посылками не найден.
     */
    @ShellMethod(key = "parcels")
    public void displayParcels(@ShellOption(defaultValue = "") String name) throws FileNotFoundException {
        if (name.isBlank())
            parcelService.displayParcels();
        else
            parcelService.displayParcel(name);
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
