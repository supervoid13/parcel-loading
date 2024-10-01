package ru.liga.loading.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.liga.loading.utils.LoadingUtils;

import java.util.Arrays;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Parcel {

    private String name;
    private char symbol;
    private char[][] box;

    public Parcel(char[][] box) {
        this.box = box;
    }

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

    /**
     * Получение высоты.
     * @return высоту посылки.
     */
    public int getHeight() {
        return box.length;
    }

    /**
     * Получение максимальной ширины.
     * @return ширину посылки.
     */
    public int getMaxWidth() {
        int temp = 0;
        for (char[] layer : box) {
            int width = layer.length;
            if (width > temp)
                temp = width;
        }
        return temp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parcel parcel = (Parcel) o;
        return Objects.deepEquals(box, parcel.box);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(box);
    }
}
