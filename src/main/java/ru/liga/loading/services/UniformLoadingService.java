package ru.liga.loading.services;

import lombok.extern.slf4j.Slf4j;
import ru.liga.loading.exceptions.NotEnoughParcelsException;
import ru.liga.loading.exceptions.NotEnoughTrucksException;
import ru.liga.loading.models.Parcel;
import ru.liga.loading.models.Truck;
import ru.liga.loading.utils.ParcelUtils;

import java.util.List;

@Slf4j
public class UniformLoadingService implements LoadingService {

    private final ParcelUtils parcelUtils = new ParcelUtils();

    @Override
    public List<Truck> loadTrucksWithParcelsWithGivenTrucks(List<Parcel> parcels, List<Truck> trucks) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.debug("Method '%s' has started".formatted(methodName));

        if (!isLoadingPossible(trucks.size(), parcels)) {
            log.warn("Not enough trucks");
            throw new NotEnoughTrucksException("Need more trucks");
        }
        loadTrucksWithParcels(parcels, trucks);
        log.debug("Method '%s' has finished".formatted(methodName));

        return trucks;
    }

    private void loadTrucksWithParcels(
            List<Parcel> parcels,
            List<Truck> trucks
    ) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.debug("Method '%s' has started".formatted(methodName));

        if (parcels.size() < trucks.size()) {
            log.warn("Not enough parcels");
            throw new NotEnoughParcelsException("Need more parcels");
        }

        log.info("Preparing parcels by square");
        List<Parcel> sortedParcels = parcelUtils.prepareParcelsBySquare(parcels);

        double avgParcelsSquare = calculateAverageSquare(sortedParcels, trucks.size());

        int truckIndex = 0, layerLevel = 1;
        Truck truck = trucks.get(truckIndex);

        while (!sortedParcels.isEmpty()) {
            Parcel parcel = findMostSuitableParcel(sortedParcels, truck, avgParcelsSquare);
            int parcelsAmount = sortedParcels.size();

            if (!needToLoadMore(parcel, truck, avgParcelsSquare) && parcelsAmount != 1) {
                truckIndex = truckIndex == trucks.size() - 1 ? 0 : truckIndex + 1;
                truck = trucks.get(truckIndex);
                layerLevel = 1;
                continue;
            }

            while (true) {
                int[] widthAndIndex = truck.getEmptySpaceWidthAndIndexOnLayer(layerLevel);
                int spaceWidth = widthAndIndex[0], index = widthAndIndex[1];
                boolean isSuccessful = truck.tryLoadParcel(parcel, layerLevel, spaceWidth, index);

                if (isSuccessful) {
                    break;
                }
                layerLevel++;
            }
            sortedParcels.remove(parcel);
        }
        log.debug("Method '%s' has finished".formatted(methodName));
    }

    private Parcel findMostSuitableParcel(List<Parcel> parcels, Truck truck, double avgSquare) {
        Parcel mostSuitableParcel = null;

        int occupiedSpace = truck.getOccupiedSpaceSquare();
        double diff = Integer.MAX_VALUE;

        for (Parcel parcel : parcels) {
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

    private boolean needToLoadMore(Parcel parcel, Truck truck, double avgParcelsSquare) {
        int occupiedSpace = truck.getOccupiedSpaceSquare();
        double diffBeforeLoad = Math.abs(avgParcelsSquare - occupiedSpace);

        int occupiedSpaceIfLoad = occupiedSpace + parcel.getSquare();
        double diffIfLoad = Math.abs(avgParcelsSquare - occupiedSpaceIfLoad);

        return diffIfLoad < diffBeforeLoad;
    }

    private double calculateAverageSquare(List<Parcel> parcels, int truckAmount) {
        double totalParcelsSquare = parcels.stream()
                .map(Parcel::getSquare)
                .mapToInt(x -> x)
                .sum();

        return totalParcelsSquare / truckAmount;
    }

    @Override
    public List<Truck> loadTrucksWithParcelsWithInfiniteTrucksAmount(List<Parcel> parcels) {
        log.error("User has selected a loading model that should not be available");
        throw new UnsupportedOperationException("You need to specify the amount of trucks for uniform loading");
    }
}
