package ru.liga.loading.services;

import lombok.extern.slf4j.Slf4j;
import ru.liga.loading.models.Parcel;
import ru.liga.loading.models.Truck;
import ru.liga.loading.utils.ParcelUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class EffectiveLoadingService implements LoadingService {

    private final ParcelUtils parcelUtils = new ParcelUtils();
    private final ParcelLoader parcelLoader = new ParcelLoader();

    @Override
    public List<Truck> loadTrucksWithParcelsWithInfiniteTrucksAmount(List<Parcel> parcels) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.debug("Method '%s' has started".formatted(methodName));

        List<Truck> trucks = new ArrayList<>();

        Truck truck = new Truck();
        trucks.add(truck);

        loadTrucksWithParcels(parcels, trucks);

        log.debug("Method '%s' has finished".formatted(methodName));
        return trucks;
    }

    private void loadTrucksWithParcels(List<Parcel> parcels, List<Truck> trucks) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.debug("Method '%s' has started".formatted(methodName));

        log.info("Preparing parcels by bottom width and square");
        List<Parcel> sortedParcels = parcelUtils.prepareParcelsByBottomWidthThenSquare(parcels);
        load(sortedParcels, trucks);

        log.debug("Method '%s' has finished".formatted(methodName));
    }

    private void load(List<Parcel> parcels, List<Truck> trucks) {
        int truckIndex = 0, parcelIndex = 0, layerLevel = 1;
        Truck truck = trucks.get(truckIndex);
        int[] widthAndIndex = truck.getEmptySpaceWidthAndIndexOnLayer(layerLevel);

        while (!parcels.isEmpty()) {
            Parcel parcelGuess = parcels.get(parcelIndex);

            int spaceWidth = widthAndIndex[0], index = widthAndIndex[1];
            boolean possibleToLoad = parcelLoader.possibleToLoad(parcelGuess, truck,
                    layerLevel, spaceWidth, index);

            if (possibleToLoad) {
                parcelLoader.loadParcel(parcelGuess, truck, layerLevel, spaceWidth, index);
                parcels.remove(parcelIndex);
                parcelIndex = 0;

                while (!truck.isLayerAvailable(layerLevel) && layerLevel != Truck.HEIGHT_CAPACITY)
                    layerLevel++;

                widthAndIndex = truck.getEmptySpaceWidthAndIndexOnLayer(layerLevel);

            } else if (parcelIndex == parcels.size() - 1) {
                if (layerLevel == Truck.HEIGHT_CAPACITY) {
                    if (++truckIndex == trucks.size()) {
                        truck = new Truck();
                        trucks.add(truck);
                        parcelIndex = 0;
                        layerLevel = 1;

                    } else {
                        truck = trucks.get(truckIndex);
                        parcelIndex = 0;
                        layerLevel = 1;
                    }
                } else {
                    parcelIndex = 0;
                    layerLevel++;
                }
                widthAndIndex = truck.getEmptySpaceWidthAndIndexOnLayer(layerLevel);
            } else {
                parcelIndex++;
            }
        }
    }

    @Override
    public List<Truck> loadTrucksWithParcelsWithGivenTrucks(List<Parcel> parcels, List<Truck> trucks) {
        log.error("User has selected a loading model that should not be available");
        throw new UnsupportedOperationException("You can't specify the amount of trucks using the effective loading");
    }
}
