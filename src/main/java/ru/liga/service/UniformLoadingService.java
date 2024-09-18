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

        double avgParcelsSquare = parcelService.calculateAverageSquare(parcels);

        int truckIndex = 0, layerLevel = 1;
        Truck truck = trucks.get(truckIndex);

        int[] widthAndIndex = truck.getEmptySpaceWidthAndIndexOnLayer(layerLevel);

        while (!sortedParcels.isEmpty()) {
            Parcel parcel = findMostSuitableParcel(sortedParcels, truck, avgParcelsSquare);

            int spaceWidth = widthAndIndex[0], index = widthAndIndex[1];

            truck.tryLoadParcel(parcel, layerLevel, spaceWidth, index);

            sortedParcels.remove(parcel);

//            truckIndex = truckIndex == trucks.size() - 1 ? 0 : truckIndex + 1;
//            truck = trucks.get(truckIndex);
        }
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
