package ru.liga.loading.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.liga.loading.enums.LoadingMode;
import ru.liga.loading.exceptions.NotEnoughParcelsException;
import ru.liga.loading.exceptions.NotEnoughTrucksException;
import ru.liga.loading.models.Parcel;
import ru.liga.loading.models.Truck;
import ru.liga.loading.readers.ParcelReader;
import ru.liga.loading.readers.TruckJsonReader;
import ru.liga.loading.services.*;
import ru.liga.loading.utils.LoadingUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Slf4j
@RequiredArgsConstructor
public class LoadingController {

    private final TruckJsonReader truckJsonReader;
    private final ParcelReader parcelReader;
    private final LoadingServiceFactory loadingServiceFactory;

    /** Эндпоинт для погрузки посылок в грузовики */
    public void loadParcels() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.debug("Method '%s' has started".formatted(methodName));
        try {
            String filePath = enterFilePath();
            List<Parcel> parcels = parcelReader.readParcelsFromFile(filePath);

            LoadingMode mode = enterLoadingMode();
            LoadingService loadingService = loadingServiceFactory.createLoadingServiceFromMode(mode);

            List<Truck> trucks = loadTrucks(loadingService, parcels);
            displayTrucks(trucks);
        } catch (IOException e) {
            System.out.println("No such file");
            log.error("File not found");
        } catch (NotEnoughTrucksException | NotEnoughParcelsException e) {
            System.out.println(e.getMessage());
        }
        log.debug("Method '%s' has finished".formatted(methodName));
    }

    /** Эндпоинт для подсчёта посылок в грузовиках из json файла*/
    public void specifyParcels() {
        String filePath = enterFilePath();
        try {
            List<Truck> trucks = truckJsonReader.readTrucksFromJson(filePath);
            displayParcelAmountAndTruckBodies(trucks);
        } catch (IOException e) {
            System.out.println("No such file");
            log.error("File not found");
        }
    }

    private String enterFilePath() {
        System.out.println("Enter file path:");
        Scanner scanner = new Scanner(System.in);
        String filePath = scanner.nextLine();
        log.info("File path has been entered");
        return filePath;
    }

    private LoadingMode enterLoadingMode() {
        LoadingMode mode;
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter mode (simple - 1, effective - 2, uniform - 3):");
            int modeInt = scanner.nextInt();
            mode = LoadingMode.values()[modeInt - 1];
        } catch (RuntimeException e) {
            log.warn("Entered invalid mode");
            throw new UnsupportedOperationException("No such mode");
        }
        log.info("Mode has been entered");
        return mode;
    }

    private List<Truck> loadTrucks(LoadingService loadingService, List<Parcel> parcels) {
        log.info("Loading process has started");
        List<Truck> trucks;
        if (loadingService instanceof UniformLoadingService ) {
            int trucksAmount = -1;
            try (Scanner scanner = new Scanner(System.in)) {
                System.out.println("Enter amount of trucks to load:");
                trucksAmount = scanner.nextInt();
            }
            List<Truck> emptyTrucks = LoadingUtils.generateEmptyTrucks(trucksAmount);
            trucks = loadingService.loadTrucksWithParcelsWithGivenTrucks(parcels, new ArrayList<>(emptyTrucks));

        } else {
            trucks = loadingService.loadTrucksWithParcelsWithInfiniteTrucksAmount(parcels);
        }
        log.info("Loading has finished");
        return trucks;
    }

    private void displayTrucks(List<Truck> trucks) {
        log.trace("Displaying truck bodies on screen");
        for (Truck truck : trucks) {
            truck.printBody();
            System.out.println();
        }
    }

    private void displayParcelAmountAndTruckBodies(List<Truck> trucks) {
        log.trace("Displaying amount of parcels by rate and truck bodies on screen");
        for (Truck truck : trucks) {
            Map<Character, Integer> countedParcels = truck.countParcels();

            for (Map.Entry<Character, Integer> entry : countedParcels.entrySet()) {
                System.out.println("Amount of parcels with rate of " + entry.getKey() + " - " + entry.getValue());
            }
            System.out.println("Truck's body");
            truck.printBody();
        }
    }
}
