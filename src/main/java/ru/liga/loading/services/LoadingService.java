package ru.liga.loading.services;

import ru.liga.loading.models.Parcel;
import ru.liga.loading.models.Truck;

import java.util.List;
import java.util.stream.Stream;

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
