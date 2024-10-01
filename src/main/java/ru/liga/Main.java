package ru.liga;

import ru.liga.loading.controllers.AppController;

public class Main {
    public static void main(String[] args) {
        AppController appController = new AppController();
        appController.start();
    }
}