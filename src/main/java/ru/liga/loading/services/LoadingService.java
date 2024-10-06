package ru.liga.loading.services;

import ru.liga.loading.models.Parcel;
import ru.liga.loading.models.Truck;

import java.util.List;

public interface LoadingService {

    /**
     * Метод погрузки посылок без ограничений на количество грузовиков.
     * @param parcels список посылок.
     * @return список погруженных грузовиков.
     */
    List<Truck> loadTrucksWithParcelsWithInfiniteTrucksAmount(List<Parcel> parcels, int truckWidth, int truckHeight);

    /**
     * Метод погрузки посылок в указанные грузовики.
     * @param parcels список посылок.
     * @param trucks список грузовиков.
     * @return список погруженных грузовиков.
     */
    List<Truck> loadTrucksWithParcelsWithGivenTrucks(List<Parcel> parcels, List<Truck> trucks);

    /**
     * Метод определения возможности погрузки списка посылок.
     * @param trucks грузовики, которые нужно загрузить.
     * @param parcels список посылок.
     * @return {@code true} если возможно погрузить все посылки в заданное количество грузовиков, {@code false}
     * в противном случае.
     */
    default boolean isLoadingPossible(List<Truck> trucks, List<Parcel> parcels) {
        int trucksBodyTotalSquare = trucks.stream()
                .map(Truck::calculateEmptySpaceSquare)
                .mapToInt(x -> x)
                .sum();

        int parcelsBoxesTotalSquare = parcels.stream()
                .map(Parcel::getSquare)
                .mapToInt(x -> x)
                .sum();

        return parcelsBoxesTotalSquare <= trucksBodyTotalSquare;
    }
}
