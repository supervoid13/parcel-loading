package ru.liga.service;

import ru.liga.exceptions.NotEnoughParcelsException;
import ru.liga.exceptions.NotEnoughTrucksException;
import ru.liga.model.Parcel;
import ru.liga.model.Truck;

import java.util.ArrayList;
import java.util.List;

public class UniformLoadingService implements LoadingService {

    private final ParcelService parcelService = new ParcelService();

    @Override
    public List<Truck> loadTrucksWithParcelsWithGivenTrucks(List<Parcel> parcels, List<Truck> trucks) {
        if (!isLoadingPossible(trucks.size(), parcels)) {
            throw new NotEnoughTrucksException("Need more trucks");
        }
        loadTrucksWithParcels(parcels, trucks);

        return trucks;
    }

    private void loadTrucksWithParcels(
            List<Parcel> parcels,
            List<Truck> trucks
    ) {
        if (parcels.size() < trucks.size()) throw new NotEnoughParcelsException("Need more parcels");

        List<Parcel> sortedParcels = parcelService.prepareParcels(parcels);

        double avgParcelsSquare = calculateAverageSquare(parcels, trucks.size());

        int truckIndex = 0, layerLevel = 1;
        Truck truck = trucks.get(truckIndex);

        int[] widthAndIndex = truck.getEmptySpaceWidthAndIndexOnLayer(layerLevel);

        while (!sortedParcels.isEmpty()) {
            Parcel parcel = findMostSuitableParcel(sortedParcels, truck, avgParcelsSquare);

            if (!needToLoadMore(parcel, truck, avgParcelsSquare)) {
                truckIndex = truckIndex == trucks.size() - 1 ? 0 : truckIndex + 1;
                truck = trucks.get(truckIndex);
                layerLevel = 1;
            }

            while (true) {
                int spaceWidth = widthAndIndex[0], index = widthAndIndex[1];
                boolean isSuccessful = truck.tryLoadParcel(parcel, layerLevel, spaceWidth, index);

                if (isSuccessful) break;

                widthAndIndex = truck.getEmptySpaceWidthAndIndexOnLayer(++layerLevel);
            }

            sortedParcels.remove(parcel);

//            truckIndex = truckIndex == trucks.size() - 1 ? 0 : truckIndex + 1;
//            truck = trucks.get(truckIndex);
        }
    }

    private double calculateAverageSquare(List<Parcel> parcels, int truckAmount) {
        double totalParcelsSquare = parcels.stream()
                .map(Parcel::getSquare)
                .mapToInt(x -> x)
                .average()
                .orElse(0d);

        return totalParcelsSquare / truckAmount;
    }

    private boolean needToLoadMore(Parcel parcel, Truck truck, double avgParcelsSquare) {
        int occupiedSpace = truck.getOccupiedSpaceSquare();
        double diffBeforeLoad = Math.abs(avgParcelsSquare - occupiedSpace);

        int occupiedSpaceIfLoad = occupiedSpace + parcel.getSquare();
        double diffIfLoad = Math.abs(avgParcelsSquare - occupiedSpaceIfLoad);

        return diffIfLoad < diffBeforeLoad;
    }

    private Parcel findMostSuitableParcel(List<Parcel> parcels, Truck truck, double avgSquare) {
//        Parcel parcel = parcels.get(0), parcelTemp;
//
//        int occupiedSpace = truck.getOccupiedSpaceSquare(), occupiedSpaceIfLoad = occupiedSpace + parcel.getSquare();
//        double diff = Math.abs(avgSquare - occupiedSpaceIfLoad);
//
//        for (int i = 1; i < parcels.size(); i++) {
//            parcelTemp = parcels.get(i);
//            occupiedSpaceIfLoad = occupiedSpace + pa
//
//            if (!(Math.abs(avgSquare - parcelTemp.getSquare()) < diff))
//                break;
//
//            parcel = parcelTemp;
//            }

        Parcel mostSuitableParcel = null;

        int occupiedSpace = truck.getOccupiedSpaceSquare();
        double diff = Integer.MAX_VALUE;

        for (Parcel parcel: parcels) {
            int occupiedSpaceIfLoad = occupiedSpace + parcel.getSquare();
            double currentDiff = Math.abs(avgSquare - occupiedSpaceIfLoad);

            if (!(currentDiff < diff)) {
                break;
            }

            mostSuitableParcel = parcel;
            diff = currentDiff;
        }

        return mostSuitableParcel;
    }

    @Override
    public List<Truck> loadTrucksWithParcelsWithInfiniteTrucksAmount(List<Parcel> parcels) {
        throw new UnsupportedOperationException();
    }
}
