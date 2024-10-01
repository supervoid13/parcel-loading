package ru.liga.loading.validators;

import org.springframework.stereotype.Component;
import ru.liga.loading.models.Truck;

@Component
public class TruckSpaceValidator {

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
    public boolean isBottomLayerValid(Truck truck, int parcelBottomWidth, int bottomLayerLevel, int index) {
        if (bottomLayerLevel == 0) return true;

        int supportWidthCounter = 0;
        char[] layer = truck.getLayer(bottomLayerLevel);

        for (int i = index; i < truck.getWidth(); i++) {
            if (layer[i] != Truck.EMPTY_SPACE_DESIGNATION)
                supportWidthCounter++;
        }
        return supportWidthCounter > parcelBottomWidth / 2;
    }
}
