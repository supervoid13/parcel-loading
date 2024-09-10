package ru.liga.service;

import ru.liga.model.Parcel;
import ru.liga.model.Truck;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EffectiveLoadingService implements LoadingService {

    @Override
    public List<Truck> loadTrucksWithParcels(List<Parcel> parcels) {
        List<Truck> trucks = new ArrayList<>();

        Truck truck = new Truck();

        List<Parcel> sortedParcels = new ArrayList<>(parcels);
        sortedParcels.sort(Comparator.comparing(Parcel::getBottomWidth)
                .thenComparing(Parcel::getSquare)
                .reversed());

        int i = 0, layerLevel = 1;

        int[] widthAndIndex = truck.getEmptySpaceWidthAndIndexOnLayer(layerLevel);

        while (!sortedParcels.isEmpty()) {
            Parcel parcelGuess = sortedParcels.get(i);

            int spaceWidth = widthAndIndex[0], index = widthAndIndex[1];

            boolean isSuccessful = truck.tryLoadParcel(parcelGuess, layerLevel, spaceWidth, index);

            if (isSuccessful) {
                sortedParcels.remove(i);
                i = 0;

                while (!truck.isLayerAvailable(layerLevel) && layerLevel != Truck.HEIGHT_CAPACITY)
                    layerLevel++;

                widthAndIndex = truck.getEmptySpaceWidthAndIndexOnLayer(layerLevel);

            } else if (i == sortedParcels.size() - 1) {
                if (layerLevel == Truck.HEIGHT_CAPACITY) {
                    trucks.add(truck);
                    truck = new Truck();
                    i = 0;
                    layerLevel = 1;
                } else {
                    i = 0;
                    layerLevel++;
                }
                widthAndIndex = truck.getEmptySpaceWidthAndIndexOnLayer(layerLevel);
            } else {
                i++;
            }
        }
        trucks.add(truck);

        return trucks;
    }
}
