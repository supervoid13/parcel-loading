package ru.liga.loading.utils;

import ru.liga.loading.models.Truck;

public class TruckUtils {

    /**
     * Метод проверки валидности уровня под уровнем {@code bottomLayerLevel} в грузовике {@code} truck для загрузки посылки с шириной
     * основания {@code parcelBottomWidth}.
     *
     * @param truck грузовик для проверки уровня
     * @param parcelBottomWidth ширина основания посылки
     * @param bottomLayerLevel  уровень в кузове, на который идёт попытка загрузить посылку
     * @param index             индекс на уровне {@code bottomLaterLevel} с которого идёт загрузка посылки
     * @return {@code true} если ширина занятого пространства под посылкой больше половины ширины основания посылки
     */
    public static boolean isBottomLayerValid(Truck truck, int parcelBottomWidth, int bottomLayerLevel, int index) {
        if (bottomLayerLevel == 0) return true;

        int supportWidthCounter = 0;
        char[] layer = truck.getLayer(bottomLayerLevel);

        for (int i = index; i < Truck.WIDTH_CAPACITY; i++) {
            if (layer[i] != Truck.EMPTY_SPACE_DESIGNATION)
                supportWidthCounter++;
        }
        return supportWidthCounter > parcelBottomWidth / 2;
    }
}
