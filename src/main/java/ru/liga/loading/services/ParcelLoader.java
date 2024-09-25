package ru.liga.loading.services;

import ru.liga.loading.models.Parcel;
import ru.liga.loading.models.Truck;
import ru.liga.loading.utils.TruckUtils;

public class ParcelLoader {

    /**
     * Метод погрузки посылки {@code parcel} на указанный уровень {@code layerLevel} в свободное место
     * шириной {@code spaceWidth}, начиная с индекса {@code index}
     *
     * @param parcel     посылка, которую нужно загрузить.
     * @param layerLevel уровень, на который нужно загрузить посылку.
     * @param spaceWidth ширина свободного места на указанном уровне.
     * @param index      индекс, с которого нужно начать загрузку.
     * @return {@code true} если загрузка прошла успешно, {@code false} в противном случае.
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
        int parcelBottomWidth = parcel.getBottomWidth();

        return !(spaceWidth < parcelBottomWidth
                || !TruckUtils.isBottomLayerValid(truck, parcelBottomWidth, layerLevel - 1, index));
    }
}
