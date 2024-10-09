package ru.liga.loading.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.liga.loading.dto.ResponseInfoDto;
import ru.liga.loading.enums.LoadingMode;
import ru.liga.loading.enums.ParcelListDefinitionMode;
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
import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/loading-service")
public class LoadingRestController {

    private final TruckJsonReader truckJsonReader;
    private final LoadingServiceFactory loadingServiceFactory;
    private final TruckService truckService;
    private final ParcelService parcelService;

    /**
     * Эндпоинт для погрузки посылок в грузовики.
     * @param loadingMode способ погрузки.
     * @param parcelListMode способ определения списка посылок.
     * @param trucks количество грузовиков (обязательно для метода UNIFORM, в остальных случаях игнорируется).
     * @param height высота кузова грузовиков (по умолчанию 6).
     * @param width ширина кузова грузовиков (по умолчанию 6).
     * @param parcels список посылок.
     * @param parcelNames список имён посылок.
     * @return список погруженных грузовиков.
     */
    @PostMapping("/load")
    public ResponseEntity<?> loadParcels(
            @RequestParam String loadingMode,
            @RequestParam String parcelListMode,
            @RequestParam(defaultValue = "-1") int trucks,
            @RequestParam(defaultValue = "6") int height,
            @RequestParam(defaultValue = "6") int width,
            @RequestBody(required = false) List<Parcel> parcels,
            @RequestBody(required = false) List<String> parcelNames
            ) {
        log.debug("Method '{}' has started", "loadParcels");

        ParcelListDefinitionMode parcelsListModeDef;
        LoadingMode loadingModeDef;
        try {
            parcelsListModeDef = ParcelListDefinitionMode.valueOf(parcelListMode.toUpperCase());
            loadingModeDef = LoadingMode.valueOf(loadingMode.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new NoSuchElementException("No such mode");
        }

        if (parcelsListModeDef == ParcelListDefinitionMode.NAMES) {
            parcels = parcelService.getParcelsByNames(parcelNames);
        }

        LoadingService loadingService = loadingServiceFactory.createLoadingServiceFromMode(loadingModeDef);

        List<Truck> loadedTrucks = loadingService.loadTrucks(parcels, trucks, width, height);

        log.debug("Method '{}' has finished", "loadParcels");
        return ResponseEntity.ok(loadedTrucks);
    }

    /**
     * Эндпоинт для подсчёта посылок в грузовиках из json файла.
     * @param trucksJsonString тело запроса в виде строки.
     * @return удобно-читаемую строку с подсчётом посылок и кузовами грузовиков.
     */
    @PostMapping("/specify")
    public ResponseEntity<?> specifyParcels(@RequestBody String trucksJsonString) {
        List<Truck> trucks = truckJsonReader.readTrucksFromJsonString(trucksJsonString);
        String prettyOutputForTrucks = truckService.getPrettyOutputForTrucks(trucks);

        return ResponseEntity.ok(prettyOutputForTrucks);
    }

    /**
     * Эндпоинт для получения всех посылок.
     * @return список посылок.
     */
    @GetMapping("/parcels")
    public ResponseEntity<?> retrieveAllParcels() {
        List<Parcel> parcels = parcelService.getAllParcels();

        return ResponseEntity.ok(parcels);
    }

    /**
     * Эндпоинт для получения посылки по её имени.
     * @param name имя посылки.
     * @return посылку с указанным именем.
     */
    @GetMapping("/parcels/{name}")
    public ResponseEntity<?> retrieveParcelByName(@PathVariable String name) {
        Parcel parcelByName = parcelService.getParcelByName(name);

        return ResponseEntity.ok(parcelByName);
    }

    /**
     * Эндпоинт для создания новой посылки.
     * @param parcel посылка.
     * @return {@code ResponseInfoDto} с ответом.
     */
    @PostMapping("/parcels")
    public ResponseEntity<?> createParcel(@RequestBody Parcel parcel) {
        parcelService.saveParcel(parcel);
        return new ResponseEntity<>(
                new ResponseInfoDto(HttpStatus.CREATED.value(), "Parcel successfully created"),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/parcels/{name}")
    public ResponseEntity<?> updateParcel(@PathVariable String name, @RequestBody Parcel parcel) {
        parcelService.updateParcelHavingBox(name, parcel);

        return ResponseEntity.ok(new ResponseInfoDto(HttpStatus.OK.value(), "Parcel successfully updated"));
    }

    @DeleteMapping("/parcels/{name}")
    public ResponseEntity<?> deleteParcel(@PathVariable String name) {
        parcelService.deleteParcel(name);

        return ResponseEntity.ok(new ResponseInfoDto(HttpStatus.OK.value(), "Parcel successfully deleted"));
    }
}
