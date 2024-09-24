package ru.liga;

import lombok.extern.slf4j.Slf4j;
import ru.liga.loading.controllers.LoadingController;
import ru.liga.loading.readers.ParcelReader;
import ru.liga.loading.readers.TruckJsonReader;
import ru.liga.loading.services.LoadingServiceFactory;

import java.util.NoSuchElementException;
import java.util.Scanner;

@Slf4j
public class Main {
    public static void main(String[] args) {
        log.info("Application has started");

        LoadingController loadingController = new LoadingController(
                new TruckJsonReader(),
                new ParcelReader(),
                new LoadingServiceFactory()
        );

        try(Scanner scanner = new Scanner(System.in)) {
            System.out.println("1 - load parcels, 2 - specify parcels");
            int func = scanner.nextInt();
            switch (func) {
                case 1:
                    loadingController.loadParcels();
                    break;
                case 2:
                    loadingController.specifyParcels();
                    break;
                default:
                    System.out.println("No such function");
                    log.warn("Unknown function has been entered");
            }
        } catch (NoSuchElementException e) {
            log.warn("Not number has been entered");
            System.out.println("You have to enter the NUMBER");
        }
        log.info("Application has finished");
    }
}
