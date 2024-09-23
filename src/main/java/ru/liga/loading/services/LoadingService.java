package ru.liga.loading.services;

import ru.liga.loading.models.Parcel;
import ru.liga.loading.models.Truck;

import java.util.List;
import java.util.stream.Stream;

public interface LoadingService {

    /**
     * Метод погрузки посылок без ограничений на количество грузовиков.
     * @param parcels список посылок.
     * @return список погруженных грузовиков.
     */
    List<Truck> loadTrucksWithParcelsWithInfiniteTrucksAmount(List<Parcel> parcels);

    /**
     * Метод погрузки посылок в указанные грузовики.
     * @param parcels список посылок.
     * @param trucks список грузовиков.
     * @return список погруженных грузовиков.
     */
    List<Truck> loadTrucksWithParcelsWithGivenTrucks(List<Parcel> parcels, List<Truck> trucks);

    /**
     * Метод определения возможности погрузки списка посылок.
     * @param trucksAmount количество грузовиков.
     * @param parcels список посылок.
     * @return {@code true} если возможно погрузить все посылки в заданное количество грузовиков, {@code false}
     * в противном случае.
     */
    default boolean isLoadingPossible(int trucksAmount, List<Parcel> parcels) {
        int trucksBodyTotalSquare = trucksAmount * Truck.BODY_SQUARE;

        int parcelsBoxesTotalSquare = parcels.stream()
                .map(Parcel::getSquare)
                .mapToInt(x -> x)
                .sum();

        return parcelsBoxesTotalSquare <= trucksBodyTotalSquare;
    }
}
