package ru.liga;

import ru.liga.model.Parcel;
import ru.liga.model.Truck;
import ru.liga.service.EffectiveLoadingService;
import ru.liga.service.LoadingService;
import ru.liga.service.ParcelService;
import ru.liga.service.SimpleLoadingService;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ParcelService parcelService = new ParcelService();
        LoadingService loadingService;
        String filePath;
        int mode = 0;


        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Enter file path:");
            filePath = scanner.nextLine();

            List<Parcel> parcels = parcelService.readParcelsFromFile(filePath);

            System.out.println("Enter mode (simple - 1, effective - 2):");
            mode = scanner.nextInt();

            switch (mode) {
                case 1:
                    loadingService = new SimpleLoadingService();
                    break;
                case 2:
                    loadingService = new EffectiveLoadingService();
                    break;
                default:
                    System.out.println("No such mode");
                    return;
            }
            List<Truck> trucks = loadingService.loadTrucksWithParcels(parcels);

            for (Truck truck: trucks) {
                truck.printBody();
                System.out.println();
            }
        } catch (IOException e) {
            System.out.println("No such file");
        }
    }
}