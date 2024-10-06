package ru.liga.loading.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.liga.loading.dto.ParcelDefinition;
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
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/loading")
public class LoadingRestController {

    private final TruckJsonReader truckJsonReader;
    private final LoadingServiceFactory loadingServiceFactory;
    private final TruckService truckService;
    private final ParcelService parcelService;

    @PostMapping
    public List<Truck> loadParcels(
            @RequestParam String mode,
            @RequestParam(defaultValue = "-1") int trucks,
            @RequestParam(defaultValue = "6") int height,
            @RequestParam(defaultValue = "6") int width,
            @RequestBody ParcelDefinition parcelDefinition
            ) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.debug("Method '%s' has started".formatted(methodName));

        String[] names = parcelDefinition.getNames();
        String file = parcelDefinition.getFile();

        if (!(file == null | names == null))
            throw new UnsupportedOperationException("You must enter no more and no less than " +
                    "1 parcels source (file/names)");

        List<Parcel> parcelList = file == null ? parcelService.getParcelsByNames(names)
                : parcelService.readParcelsFromFile(file);

        LoadingMode loadingMode = LoadingMode.valueOf(mode.toUpperCase());
        LoadingService loadingService = loadingServiceFactory.createLoadingServiceFromMode(loadingMode);

        List<Truck> loadedTrucks = loadTrucks(loadingService, parcelList, trucks, width, height);

        log.debug("Method '%s' has finished".formatted(methodName));
        return loadedTrucks;
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
        if (loadingService instanceof UniformLoadingService) {
            List<Truck> emptyTrucks = LoadingUtils.generateEmptyTrucks(trucks, width, height);
            loadedTrucks = loadingService.loadTrucksWithParcelsWithGivenTrucks(parcels, new ArrayList<>(emptyTrucks));
        } else {
            loadedTrucks = loadingService.loadTrucksWithParcelsWithInfiniteTrucksAmount(parcels, width, height);
        }
        log.info("Loading has finished");
        return loadedTrucks;
    }
}
