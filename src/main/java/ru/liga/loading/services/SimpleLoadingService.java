package ru.liga.loading.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.liga.loading.exceptions.NotEnoughSpaceException;
import ru.liga.loading.models.Parcel;
import ru.liga.loading.models.Truck;
import ru.liga.loading.utils.ParcelLoader;
import ru.liga.loading.utils.ParcelUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component("simple")
public class SimpleLoadingService implements LoadingService {

    private final ParcelUtils parcelUtils;
    private final ParcelLoader parcelLoader;

    private static final int BOTTOM_LAYER_LEVEL = 1;
    private static final int LAYER_FIRST_INDEX = 0;

    @Override
    public List<Truck> loadTrucksWithParcelsWithInfiniteTrucksAmount(List<Parcel> parcels, int truckWidth, int truckHeight) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.debug("Method '{}' has started", "loadTrucksWithParcelsWithInfiniteTrucksAmount");

        List<Parcel> sortedParcels = parcelUtils.prepareParcelsBySquare(parcels);
        List<Truck> trucks = load(sortedParcels, truckWidth, truckHeight);

        log.debug("Method '{}' has finished", "loadTrucksWithParcelsWithInfiniteTrucksAmount");
        return trucks;
    }

    private List<Truck> load(List<Parcel> parcels, int truckWidth, int truckHeight) {
        parcelLoader.checkParcelsFitTruckBodies(parcels, truckHeight, truckWidth);

        List<Truck> trucks = new ArrayList<>();
        Truck truck = new Truck(truckWidth, truckHeight);
        int i = 0;
        while (!parcels.isEmpty()) {
            Parcel parcelGuess = parcels.get(i);
            boolean possibleToLoad = parcelLoader.possibleToLoad(parcelGuess, truck,
                    BOTTOM_LAYER_LEVEL, truck.getWidth(), LAYER_FIRST_INDEX);

            if (!possibleToLoad) {
                log.error("Parcel does not fit truck body");
                throw new NotEnoughSpaceException("Parcel doesn't fit truck body");
            }
            parcelLoader.loadParcel(parcelGuess, truck, BOTTOM_LAYER_LEVEL, truck.getWidth(), LAYER_FIRST_INDEX);
            trucks.add(truck);
            truck = new Truck(truckWidth, truckHeight);
            parcels.remove(i);
        }
        return trucks;
    }

    @Override
    public List<Truck> loadTrucksWithParcelsWithGivenTrucks(List<Parcel> parcels, List<Truck> trucks) {
        log.error("User has selected a loading model that should not be available");
        throw new UnsupportedOperationException("You can't specify the amount of trucks using the simple loading");
    }
}
