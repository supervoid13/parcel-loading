package ru.liga.loading.controllers;

import lombok.extern.slf4j.Slf4j;
import ru.liga.loading.exceptions.NotEnoughParcelsException;
import ru.liga.loading.exceptions.NotEnoughTrucksException;
import ru.liga.loading.models.Parcel;
import ru.liga.loading.models.Truck;
import ru.liga.loading.readers.ParcelReader;
import ru.liga.loading.readers.TruckJsonReader;
import ru.liga.loading.services.EffectiveLoadingService;
import ru.liga.loading.services.LoadingService;
import ru.liga.loading.services.SimpleLoadingService;
import ru.liga.loading.services.UniformLoadingService;
import ru.liga.loading.utils.LoadingUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Slf4j
public class LoadingController {

    private final TruckJsonReader truckJsonReader = new TruckJsonReader();

    /** Эндпоинт для погрузки посылок в грузовики */
    public void loadParcels() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.debug("Method '%s' has started".formatted(methodName));

        ParcelReader parcelReader = new ParcelReader();
        LoadingService loadingService;
        String filePath;
        int mode = 0, trucksAmount = -1;


        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Enter file path:");
            filePath = scanner.nextLine();
            log.info("File path has been entered");

            List<Parcel> parcels = parcelReader.readParcelsFromFile(filePath);

            System.out.println("Enter mode (simple - 1, effective - 2, uniform - 3):");
            mode = scanner.nextInt();
            log.info("Mode has been entered");

            switch (mode) {
                case 1:
                    log.info("Mode - simple loading");
                    loadingService = new SimpleLoadingService();
                    break;
                case 2:
                    log.info("Mode - effective loading");
                    loadingService = new EffectiveLoadingService();
                    break;
                case 3:
                    log.info("Mode - uniform loading");
                    loadingService = new UniformLoadingService();
                    System.out.println("Enter amount of trucks to load (negative for unlimited amount)");
                    trucksAmount = scanner.nextInt();
                    break;
                default:
                    System.out.println("No such mode");
                    log.warn("Entered invalid mode");
                    return;
            }

            List<Truck> trucks;

            if (trucksAmount < 0) {
                log.info("Amount of trucks - infinite");
                trucks = loadingService.loadTrucksWithParcelsWithInfiniteTrucksAmount(parcels);
            } else {
                log.info("Amount of trucks - %d".formatted(trucksAmount));
                List<Truck> emptyTrucks = LoadingUtils.generateEmptyTrucks(trucksAmount);

                log.info("Loading has started");
                try {
                    trucks = loadingService.loadTrucksWithParcelsWithGivenTrucks(parcels, new ArrayList<>(emptyTrucks));
                } catch (NotEnoughTrucksException | NotEnoughParcelsException e) {
                    System.out.println(e.getMessage());
                    return;
                }
                log.info("Loading has finished");
            }

            log.trace("Displaying truck bodies on screen");
            for (Truck truck : trucks) {
                truck.printBody();
                System.out.println();
            }

        } catch (IOException e) {
            System.out.println("No such file");
            log.error("File not found");
        }
        log.debug("Method '%s' has finished".formatted(methodName));
    }

    /** Эндпоинт для подсчёта посылок в грузовиках из json файла*/
    public void specifyParcels() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Enter truck json file path:");
            String filePath = scanner.nextLine();

            List<Truck> trucks = truckJsonReader.readTrucksFromJson(filePath);

            log.trace("Displaying amount of parcels by rate and truck bodies on screen");
            for (Truck truck : trucks) {
                Map<Character, Integer> countedParcels = truck.countParcels();

                for (Map.Entry<Character, Integer> entry : countedParcels.entrySet()) {
                    System.out.println("Amount of parcels with rate of " + entry.getKey() + " - " + entry.getValue());
                }
                System.out.println("Truck's body");
                truck.printBody();
            }

        } catch (IOException e) {
            System.out.println("No such file");
            log.error("File not found");
        }
    }
}
