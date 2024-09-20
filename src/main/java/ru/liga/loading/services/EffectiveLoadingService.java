package ru.liga.loading.services;

import lombok.extern.slf4j.Slf4j;
import ru.liga.loading.exceptions.NotEnoughTrucksException;
import ru.liga.loading.models.Parcel;
import ru.liga.loading.models.Truck;
import ru.liga.loading.readers.ParcelReader;
import ru.liga.loading.utils.ParcelUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class EffectiveLoadingService implements LoadingService {

    private final ParcelReader parcelReader = new ParcelReader();
    private final ParcelUtils parcelUtils = new ParcelUtils();

    @Override
    public List<Truck> loadTrucksWithParcelsWithInfiniteTrucksAmount(List<Parcel> parcels) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.debug("Method '%s' has started".formatted(methodName));

        List<Truck> trucks = new ArrayList<>();

        Truck truck = new Truck();
        trucks.add(truck);

        loadTrucksWithParcels(parcels, trucks, true);

        log.debug("Method '%s' has finished".formatted(methodName));
        return trucks;
    }

    @Override
    public List<Truck> loadTrucksWithParcelsWithGivenTrucks(List<Parcel> parcels, List<Truck> trucks) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.debug("Method '%s' has started".formatted(methodName));

        if (!isLoadingPossible(trucks.size(), parcels)) {
            log.error("Not enough trucks");
            throw new NotEnoughTrucksException("Need more trucks");
        }
        loadTrucksWithParcels(parcels, trucks, false);

        log.debug("Method '%s' has finished".formatted(methodName));
        return trucks;
    }

    private void loadTrucksWithParcels(
            List<Parcel> parcels,
            List<Truck> trucks,
            boolean allowNewTrucks
    ) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.debug("Method '%s' has started".formatted(methodName));

        log.info("Preparing parcels by bottom width and square");
        List<Parcel> sortedParcels = parcelUtils.prepareParcelsByBottomWidthThenSquare(parcels);

        int truckIndex = 0, parcelIndex = 0, layerLevel = 1;

        Truck truck = trucks.get(truckIndex);

        int[] widthAndIndex = truck.getEmptySpaceWidthAndIndexOnLayer(layerLevel);

        while (!sortedParcels.isEmpty()) {
            Parcel parcelGuess = sortedParcels.get(parcelIndex);

            int spaceWidth = widthAndIndex[0], index = widthAndIndex[1];

            boolean isSuccessful = truck.tryLoadParcel(parcelGuess, layerLevel, spaceWidth, index);

            if (isSuccessful) {
                sortedParcels.remove(parcelIndex);
                parcelIndex = 0;

                while (!truck.isLayerAvailable(layerLevel) && layerLevel != Truck.HEIGHT_CAPACITY)
                    layerLevel++;

                widthAndIndex = truck.getEmptySpaceWidthAndIndexOnLayer(layerLevel);

            } else if (parcelIndex == sortedParcels.size() - 1) {
                if (layerLevel == Truck.HEIGHT_CAPACITY) {
                    if (++truckIndex == trucks.size()) {
                        if (allowNewTrucks) {
                            truck = new Truck();
                            trucks.add(truck);
                            parcelIndex = 0;
                            layerLevel = 1;
                        } else {
                            log.error("Not enough trucks");
                            throw new NotEnoughTrucksException("Need more trucks");
                        }
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
        log.debug("Method '%s' has finished".formatted(methodName));
    }
}
