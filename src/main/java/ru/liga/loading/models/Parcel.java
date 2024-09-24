package ru.liga.loading.models;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import ru.liga.loading.utils.LoadingUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@EqualsAndHashCode
public class Parcel {

    private final char[][] box;

    /**
     * Получение значения поля {@link Parcel#box}
     * @return копия тела посылки
     */
    public char[][] getBox() {
        return LoadingUtils.getArrayCopy(box);
    }

    /**
     * Получение ширины нижнего слоя посылки
     * @return ширина нижнего слоя посылки
     */
    public int getBottomWidth() {
        int height = box.length;

        return box[height - 1].length;
    }

    /**
     * Получение площади посылки
     * @return общая площадь посылки
     */
    public int getSquare() {
        int result = 0;

        for (char[] charArr : box) {
            result += charArr.length;
        }
        return result;
    }
}
