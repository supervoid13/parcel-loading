package ru.liga.loading.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.liga.loading.dto.LoadingDataDto;
import ru.liga.loading.dto.ParcelDto;
import ru.liga.loading.dto.TruckDto;
import ru.liga.loading.enums.LoadingMode;
import ru.liga.loading.models.Parcel;
import ru.liga.loading.models.Truck;
import ru.liga.loading.readers.TruckJsonReader;
import ru.liga.loading.services.LoadingService;
import ru.liga.loading.services.LoadingServiceFactory;
import ru.liga.loading.services.ParcelService;
import ru.liga.loading.services.TruckService;
import ru.liga.loading.validators.ParcelValidator;
import ru.liga.loading.validators.TruckValidator;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/loading-service")
@Validated
public class LoadingRestController {

    private final LoadingServiceFactory loadingServiceFactory;
    private final TruckJsonReader truckJsonReader;
    private final TruckValidator truckValidator;
    private final TruckService truckService;
    private final ParcelService parcelService;
    private final ParcelValidator parcelValidator;
    private final ModelMapper modelMapper;

    /**
     * Эндпоинт для погрузки посылок в грузовики.
     * @param loadingDataDto данные погрузки.
     * @return список погруженных грузовиков.
     */
    @PostMapping("/load")
    public List<Truck> loadParcels(@Valid @RequestBody LoadingDataDto loadingDataDto) {
        log.debug("Method '{}' has started", "loadParcels");

        LoadingMode loadingMode = loadingDataDto.getMode();
        int trucks = loadingDataDto.getTrucks(), height = loadingDataDto.getHeight(), width = loadingDataDto.getWidth();
        List<Parcel> parcels = loadingDataDto.getParcels();

        if (parcels == null) {
            parcels = parcelService.getParcelsByNames(loadingDataDto.getParcelNames());
        }

        LoadingService loadingService = loadingServiceFactory.createLoadingServiceFromMode(loadingMode);

        List<Truck> loadedTrucks = loadingService.loadTrucks(parcels, trucks, width, height);

        log.debug("Method '{}' has finished", "loadParcels");
        return loadedTrucks;
    }

    /**
     * Эндпоинт для подсчёта посылок в грузовиках из json файла.
     * @param truckDtos список грузовиков.
     * @return удобно-читаемую строку с подсчётом посылок и кузовами грузовиков.
     */
    @PostMapping("/specify")
    public String specifyParcels(@RequestBody List<TruckDto> truckDtos) {
        List<Truck> trucks = modelMapper.map(truckDtos, new TypeToken<List<Truck>>() {}.getType());
        truckValidator.validateTruckList(trucks);

        return truckService.getPrettyOutputForTrucks(trucks);
    }

    /**
     * Эндпоинт для получения всех посылок.
     * @return список посылок.
     */
    @GetMapping("/parcels")
    public List<ParcelDto> retrieveAllParcels() {
        List<Parcel> parcels = parcelService.getAllParcels();

        return modelMapper.map(parcels, new TypeToken<List<ParcelDto>>() {}.getType());
    }

    /**
     * Эндпоинт для получения посылки по её имени.
     * @param name имя посылки.
     * @return посылку с указанным именем.
     */
    @GetMapping("/parcels/{name}")
    public ParcelDto retrieveParcelByName(@PathVariable String name) {
        Parcel parcel = parcelService.getParcelByName(name);
        return modelMapper.map(parcel, ParcelDto.class);
    }

    /**
     * Эндпоинт для создания новой посылки.
     * @param parcelDto посылка.
     * @return созданную посылку..
     */
    @PostMapping("/parcels")
    public ParcelDto createParcel(@RequestBody ParcelDto parcelDto) {
        Parcel parcel = modelMapper.map(parcelDto, Parcel.class);
        parcelValidator.validateBox(parcel);

        Parcel parcelWithId = parcelService.saveParcel(parcel);

        return modelMapper.map(parcelWithId, ParcelDto.class);
    }

    /**
     * Эндпоинт для обновления существующей посылки.
     * @param name имя посылки.
     * @param parcelDto новые данные посылки.
     * @return обновлённую посылку.
     */
    @PutMapping("/parcels/{name}")
    public ParcelDto updateParcel(@PathVariable String name, @RequestBody ParcelDto parcelDto) {
        Parcel parcel = modelMapper.map(parcelDto, Parcel.class);
        parcelValidator.validateBox(parcel);

        Parcel updatedParcel = parcelService.updateParcelHavingBox(name, parcel);

        return modelMapper.map(updatedParcel, ParcelDto.class);
    }

    /**
     * Эндпоинт для удаления посылки.
     * @param name имя посылки.
     */
    @DeleteMapping("/parcels/{name}")
    public void deleteParcel(@PathVariable String name) {
        parcelService.deleteParcel(name);
    }
}
