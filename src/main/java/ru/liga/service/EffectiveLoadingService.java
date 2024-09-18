package ru.liga.service;

import ru.liga.exceptions.NotEnoughTrucksException;
import ru.liga.model.Parcel;
import ru.liga.model.Truck;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EffectiveLoadingService implements LoadingService {

    private final ParcelService parcelService = new ParcelService();

    @Override
    public List<Truck> loadTrucksWithParcelsWithInfiniteTrucksAmount(List<Parcel> parcels) {
        List<Truck> trucks = new ArrayList<>();

        Truck truck = new Truck();
        trucks.add(truck);

        loadTrucksWithParcels(parcels, trucks, true);

        return trucks;
    }

    @Override
    public List<Truck> loadTrucksWithParcelsWithGivenTrucks(List<Parcel> parcels, List<Truck> trucks) {
        if (!isLoadingPossible(trucks.size(), parcels)) {
            throw new NotEnoughTrucksException("Need more trucks");
        }
        loadTrucksWithParcels(parcels, trucks, false);

        return trucks;
    }

    private void loadTrucksWithParcels(
            List<Parcel> parcels,
            List<Truck> trucks,
            boolean allowNewTrucks
    ) {
       List<Parcel> sortedParcels = parcelService.prepareParcels(parcels);

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
    }
}
