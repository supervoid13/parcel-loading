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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@ShellComponent
public class LoadingShellController {

    private final TruckJsonReader truckJsonReader;
    private final LoadingServiceFactory loadingServiceFactory;
    private final TruckService truckService;
    private final ParcelService parcelService;


    /**
     * Эндпоинт для погрузки посылок в грузовики.
     * @param mode способ погрузки.
     * @param trucks количество грузовиков (обязательно для метода UNIFORM, в остальных случаях игнорируется).
     * @param height высота кузова грузовиков (по умолчанию 6).
     * @param width ширина кузова грузовиков (по умолчанию 6).
     * @param file путь к файлу.
     * @param parcels список имён посылок.
     * @return удобно-читаемую строку для списка погруженных грузовиков.
     */
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

        List<Parcel> parcelList = file.isBlank() ? parcelService.getParcelsByNames(Arrays.asList(parcels))
                : parcelService.readParcelsFromFile(file);

        LoadingMode loadingMode = LoadingMode.valueOf(mode.toUpperCase());
        LoadingService loadingService = loadingServiceFactory.createLoadingServiceFromMode(loadingMode);

        List<Truck> loadedTrucks = loadingService.loadTrucks(parcelList, trucks, width, height);

        log.debug("Method '{}' has finished", "loadParcels");
        return truckService.getPrettyOutputForTrucks(loadedTrucks);
    }

    /**
     * Эндпоинт для подсчёта посылок в грузовиках из json файла.
     * @param file путь к файлу.
     * @return удобно-читаемую строку для списка грузовиков.
     */
    @ShellMethod(key = "specify")
    public String specifyParcels(String file) {
            return truckService.getPrettyOutputForTrucks(truckJsonReader.readTrucksFromJson(file));
    }

    /**
     * Эндпоинт для отображения посылок (определённую посылку, если имя указано, все посылки - в противном случае).
     * @param name имя посылки (необязательно).
     */
    @ShellMethod(key = "parcels")
    public String displayParcels(@ShellOption(defaultValue = "") String name) {
        return name.isBlank() ? parcelService.getPrettyOutputForAllParcels()
                : parcelService.getPrettyOutputForParcelByName(name);
    }

    /**
     * Эндпоинт для создания новой посылки.
     * @param name имя посылки.
     * @param symbol символ формы.
     */
    @ShellMethod(key = "create")
    public void createParcel(String name, char symbol) {
        parcelService.createParcel(name, symbol);
    }

    /**
     * Эндпоинт для изменения существующей посылки.
     * @param name имя посылки.
     * @param newName новое имя посылки.
     * @param newSymbol новый символ посылки.
     */
    @ShellMethod(key = "update")
    public void updateParcel(
            String name,
            @ShellOption(defaultValue = "") String newName,
            @ShellOption(defaultValue = " ") char newSymbol
    ) {
        parcelService.updateParcel(name, newName, newSymbol);
    }

    /**
     * Эндпоинт для удаления существующей посылки.
     * @param name имя посылки.
     */
    @ShellMethod(key = "delete")
    public void deleteParcel(String name) {
        parcelService.deleteParcel(name);
    }
}
