package ru.liga.service;

import ru.liga.model.Parcel;
import ru.liga.model.Truck;

import java.util.List;

public interface LoadingService {

    List<Truck> loadTrucksWithParcelsWithInfiniteTrucksAmount(List<Parcel> parcels);
    List<Truck> loadTrucksWithParcelsWithGivenTrucks(List<Parcel> parcels, List<Truck> trucks);

    default boolean isLoadingPossible(int trucksAmount, List<Parcel> parcels) {
        int trucksBodyTotalSquare = trucksAmount * Truck.BODY_SQUARE;

        int parcelsBoxesTotalSquare = parcels.stream()
                .map(Parcel::getSquare)
                .mapToInt(x -> x)
                .sum();

        return parcelsBoxesTotalSquare <= trucksBodyTotalSquare;
    }
}
