package ru.liga.service;

import ru.liga.model.Parcel;
import ru.liga.model.Truck;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SimpleLoadingService implements LoadingService {

    @Override
    public List<Truck> loadTrucksWithParcelsWithInfiniteTrucksAmount(List<Parcel> parcels) {
        List<Truck> trucks = new ArrayList<>();

        Truck truck = new Truck();

        List<Parcel> sortedParcels = new ArrayList<>(parcels);
        sortedParcels.sort(Comparator.comparing(Parcel::getBottomWidth)
                .thenComparing(Parcel::getSquare)
                .reversed());

        int i = 0;

        while (!sortedParcels.isEmpty()) {
            Parcel parcelGuess = sortedParcels.get(i);

            boolean isSuccessful = truck.tryLoadParcel(parcelGuess, 1, Truck.WIDTH_CAPACITY, 0);

            if (!isSuccessful) {
                System.out.println("One of the parcels will be deleted (doesn't fit truck body)");
            } else {
                trucks.add(truck);
                truck = new Truck();
            }
            sortedParcels.remove(i);
        }
        return trucks;
    }

    @Override
    public List<Truck> loadTrucksWithParcelsWithGivenTrucks(List<Parcel> parcels, List<Truck> trucks) {
        throw new UnsupportedOperationException();
    }
}
