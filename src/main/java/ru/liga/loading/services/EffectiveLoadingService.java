package ru.liga.loading.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.liga.loading.exceptions.NotEnoughSpaceException;
import ru.liga.loading.models.Parcel;
import ru.liga.loading.models.Truck;
import ru.liga.loading.utils.ParcelLoader;
import ru.liga.loading.utils.ParcelUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component("effective")
public class EffectiveLoadingService implements LoadingService {

    private final ParcelUtils parcelUtils;
    private final ParcelLoader parcelLoader;

    @Override
    public List<Truck> loadTrucksWithParcelsWithInfiniteTrucksAmount(
            List<Parcel> parcels,
            int truckWidth,
            int truckHeight
    ) {
        log.debug("Method '{}' has started", "loadTrucksWithParcelsWithInfiniteTrucksAmount");

        List<Truck> trucks = loadTrucksWithParcels(parcels, truckWidth, truckHeight);

        log.debug("Method '{}' has finished", "loadTrucksWithParcelsWithInfiniteTrucksAmount");
        return trucks;
    }

    private List<Truck> loadTrucksWithParcels(List<Parcel> parcels, int truckWidth, int truckHeight) {
        log.debug("Method '{}' has started", "loadTrucksWithParcels");


        parcelLoader.checkParcelsFitTruckBodies(parcels, truckHeight, truckWidth);

        log.info("Preparing parcels by bottom width and square");
        List<Parcel> sortedParcels = parcelUtils.prepareParcelsByBottomWidthThenSquare(parcels);
        List<Truck> trucks = load(sortedParcels, truckWidth, truckHeight);

        log.debug("Method '{}' has finished", "loadTrucksWithParcels");
        return trucks;
    }

    private List<Truck> load(List<Parcel> parcels, int truckWidth, int truckHeight) {
        List<Truck> trucks = new ArrayList<>();

        Truck truck = new Truck(truckWidth, truckHeight);
        trucks.add(truck);

        int truckIndex = 0, parcelIndex = 0, layerLevel = 1;
        int[] widthAndIndex = truck.calcEmptySpaceWidthAndIndexOnLayer(layerLevel);

        while (!parcels.isEmpty()) {
            Parcel parcelGuess = parcels.get(parcelIndex);

            int spaceWidth = widthAndIndex[0], index = widthAndIndex[1];
            boolean possibleToLoad = parcelLoader.possibleToLoad(parcelGuess, truck,
                    layerLevel, spaceWidth, index);

            if (possibleToLoad) {
                try {
                    parcelLoader.loadParcel(parcelGuess, truck, layerLevel, spaceWidth, index);
                } catch (NotEnoughSpaceException e) {
                    truck = new Truck(truckWidth, truckHeight);
                    trucks.add(truck);
                    parcelIndex = 0;
                    layerLevel = 1;
                }
                parcels.remove(parcelIndex);
                parcelIndex = 0;

                while (!truck.isLayerAvailable(layerLevel) && layerLevel != truck.getHeight())
                    layerLevel++;

                widthAndIndex = truck.calcEmptySpaceWidthAndIndexOnLayer(layerLevel);

            } else if (parcelIndex == parcels.size() - 1) {
                if (layerLevel == truck.getHeight()) {
                    if (++truckIndex == trucks.size()) {
                        truck = new Truck(truckWidth, truckHeight);
                        trucks.add(truck);
                        parcelIndex = 0;
                        layerLevel = 1;

                    } else {
                        truck = trucks.get(truckIndex);
                        parcelIndex = 0;
                        layerLevel = 1;
                    }
                } else {
                    parcelIndex = 0;
                    layerLevel++;
                }
                widthAndIndex = truck.calcEmptySpaceWidthAndIndexOnLayer(layerLevel);
            } else {
                parcelIndex++;
            }
        }
        return trucks;
    }

    @Override
    public List<Truck> loadTrucksWithParcelsWithGivenTrucks(List<Parcel> parcels, List<Truck> trucks) {
        log.error("User has selected a loading model that should not be available");
        throw new UnsupportedOperationException("You can't specify the amount of trucks using the effective loading");
    }
}
