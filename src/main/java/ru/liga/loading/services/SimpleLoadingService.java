package ru.liga.loading.services;

import lombok.extern.slf4j.Slf4j;
import ru.liga.loading.models.Parcel;
import ru.liga.loading.models.Truck;
import ru.liga.loading.utils.ParcelUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SimpleLoadingService implements LoadingService {

    private final ParcelUtils parcelUtils = new ParcelUtils();
    private final ParcelLoader parcelLoader = new ParcelLoader();

    private static final int BOTTOM_LAYER_LEVEL = 1;
    private static final int LAYER_FIRST_INDEX = 0;

    @Override
    public List<Truck> loadTrucksWithParcelsWithInfiniteTrucksAmount(List<Parcel> parcels) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.debug("Method '%s' has started".formatted(methodName));

        List<Truck> trucks = new ArrayList<>();

        List<Parcel> sortedParcels = parcelUtils.prepareParcelsBySquare(parcels);
        load(sortedParcels, trucks);

        log.debug("Method '%s' has finished".formatted(methodName));
        return trucks;
    }

    private void load(List<Parcel> parcels, List<Truck> trucks) {
        Truck truck = new Truck();
        int i = 0;
        while (!parcels.isEmpty()) {
            Parcel parcelGuess = parcels.get(i);
            boolean possibleToLoad = parcelLoader.possibleToLoad(parcelGuess, truck,
                    BOTTOM_LAYER_LEVEL, Truck.WIDTH_CAPACITY, LAYER_FIRST_INDEX);

            if (possibleToLoad) {
                parcelLoader.loadParcel(parcelGuess, truck, BOTTOM_LAYER_LEVEL, Truck.WIDTH_CAPACITY, LAYER_FIRST_INDEX);
                trucks.add(truck);
                truck = new Truck();
            } else {
                log.error("Parcel does not fit truck body");
                System.out.println("One of the parcels will be deleted (doesn't fit truck body)");
            }
            parcels.remove(i);
        }
    }

    @Override
    public List<Truck> loadTrucksWithParcelsWithGivenTrucks(List<Parcel> parcels, List<Truck> trucks) {
        log.error("User has selected a loading model that should not be available");
        throw new UnsupportedOperationException("You can't specify the amount of trucks using the simple loading");
    }
}
