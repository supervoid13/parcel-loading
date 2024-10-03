package ru.liga.loading.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.liga.loading.exceptions.NotEnoughParcelsException;
import ru.liga.loading.exceptions.NotEnoughSpaceException;
import ru.liga.loading.models.Parcel;
import ru.liga.loading.models.Truck;
import ru.liga.loading.validators.TruckSpaceValidator;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ParcelLoader {

    private final TruckSpaceValidator truckSpaceValidator;

    /**
     * Метод погрузки посылки {@code parcel} на указанный уровень {@code layerLevel} в свободное место
     * шириной {@code spaceWidth}, начиная с индекса {@code index}
     *
     * @param parcel     посылка, которую нужно загрузить.
     * @param layerLevel уровень, на который нужно загрузить посылку.
     * @param spaceWidth ширина свободного места на указанном уровне.
     * @param index      индекс, с которого нужно начать загрузку.
     */
    public void loadParcel(Parcel parcel, Truck truck, int layerLevel, int spaceWidth, int index) {
        truck.insertParcel(parcel, layerLevel, index);
    }

    /**
     * Метод проверки возможности погрузки посылки.
     * @param parcel посылка.
     * @param truck грузовик, в который нужно погрузить посылку.
     * @param layerLevel уровень в кузове.
     * @param spaceWidth ширина свободного места.
     * @param index индекс начала свободного места.
     * @return {@code true} если возможно погрузить посылку, {@code false} в противном случае
     */
    public boolean possibleToLoad(Parcel parcel, Truck truck, int layerLevel, int spaceWidth, int index) {
        int parcelWidth = parcel.getBottomWidth();
        int parcelHeight = parcel.getHeight();

        int freeHeight = truck.getHeight() - layerLevel + 1;

        return !(spaceWidth < parcelWidth
                || freeHeight < parcelHeight
                || !truckSpaceValidator.isBottomLayerValid(truck, parcelWidth, layerLevel - 1, index));
    }

    /**
     * Метод для предварительной проверки, чтобы габариты посылок не превышали габариты кузова грузовика.
     * @param parcels список посылок.
     * @param truckHeight высота кузова.
     * @param truckWidth ширина кузова.
     * @throws NotEnoughSpaceException если габариты посылок невалидны.
     */
    public void checkParcelsFitTruckBodies(List<Parcel> parcels, int truckHeight, int truckWidth) {
        int maxParcelHeight = parcels.stream()
                .map(Parcel::getHeight)
                .mapToInt(x -> x)
                .max()
                .orElse(0);

        int maxParcelWidth = parcels.stream()
                .map(Parcel::getMaxWidth)
                .mapToInt(x -> x)
                .max()
                .orElse(0);

        if (maxParcelHeight > truckHeight || maxParcelWidth > truckWidth)
            throw new NotEnoughSpaceException("Parcel doesn't fit truck body");
    }

    /**
     * Метод предварительной проверки, достаточно ли посылок для их погрузки в грузовики.
     * @param parcels список посылок.
     * @param trucks список грузовиков.
     * @throws NotEnoughParcelsException если данных посылок недостаточно.
     */
    public void checkEnoughParcelsForTrucks(List<Parcel> parcels, List<Truck> trucks) {
        if (parcels.size() < trucks.size()) {
            log.error("Not enough parcels");
            throw new NotEnoughParcelsException("Need more parcels");
        }
    }
}
