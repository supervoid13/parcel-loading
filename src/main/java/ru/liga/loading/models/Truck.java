package ru.liga.loading.models;

import lombok.Value;
import ru.liga.loading.exceptions.NotEnoughSpaceException;
import ru.liga.loading.utils.LoadingUtils;

import java.util.Arrays;
import java.util.Objects;

@Value
public class Truck {

    public static final char EMPTY_SPACE_DESIGNATION = ' ';

    int width;
    int height;
    char[][] body;

    /**
     * Конструктор - создание нового объекта с указанной шириной и высотой.
     * @param width ширина
     * @param height высота
     */
    public Truck(int width, int height) {
        this.width = width;
        this.height = height;
        body = new char[height][width];
        for (char[] layer : body) {
            Arrays.fill(layer, EMPTY_SPACE_DESIGNATION);
        }
    }

    /**
     * Конструктор - создание нового объекта и инициализация кузова переданным кузовом.
     *
     * @param body кузов, с которым нужно создать объект грузовика.
     */
    public Truck(char[][] body) {
        height = body.length;
        width = body[0].length;
        this.body = LoadingUtils.getArrayCopy(body);
    }


    /**
     * Получение значение поля {@link Truck#body}
     *
     * @return копия кузова грузовика
     */
    public char[][] getBody() {
        return LoadingUtils.getArrayCopy(body);
    }

    /**
     * Получение занятого места в кузове.
     *
     * @return площадь кузова, занятая посылками.
     */
    public int calculateOccupiedSpaceSquare() {
        int square = 0;

        for (char[] layer : body) {
            for (char ch : layer) {
                if (ch != EMPTY_SPACE_DESIGNATION)
                    square++;
            }
        }
        return square;
    }

    /**
     * Получение свободного места в кузове.
     *
     * @return площадь кузова, свободная от посылок.
     */
    public int calculateEmptySpaceSquare() {
        return width * height - calculateOccupiedSpaceSquare();
    }

    public char[] getLayer(int layerLevel) {
        char[] layer = body[height - layerLevel];
        return Arrays.copyOf(layer, layer.length);
    }

    /**
     * Получение массива двух элементов: ширину свободного места и индекс его начала, соответственно,
     * на указанном уровне {@code layerLevel}.
     *
     * @param layerLevel уровень высоты в кузове. Начинается с 1.
     * @return массив двух элементов: ширина и индекс начала.
     */
    public int[] calcEmptySpaceWidthAndIndexOnLayer(int layerLevel) {
        char[] layer = getLayer(layerLevel);

        int maxLength = 0, maxIndex = -1, currentLength = 0, currentIndex = -1;

        for (int i = 0; i < layer.length; i++) {
            if (layer[i] == EMPTY_SPACE_DESIGNATION) {
                if (currentLength == 0) {
                    currentIndex = i;
                }
                currentLength++;
            } else {
                if (currentLength > maxLength) {
                    maxLength = currentLength;
                    maxIndex = currentIndex;
                }
                currentLength = 0;
            }
        }

        if (currentLength > maxLength) {
            maxLength = currentLength;
            maxIndex = currentIndex;
        }

        return new int[]{maxLength, maxIndex};
    }


    /**
     * Метод вставки посылки в кузов грузовика.
     *
     * @param parcel     посылка.
     * @param layerLevel уровень в кузове.
     * @param index      индекс на уровне.
     */
    public void insertParcel(Parcel parcel, int layerLevel, int index) {
        int tempIndex = index;
        int deepIndex = height - layerLevel;

        char[][] box = parcel.getBox();

        try {
            for (int i = box.length - 1; i >= 0; i--) {
                for (int j = 0; j < box[i].length; j++) {
                    body[deepIndex][index++] = box[i][j];
                }
                deepIndex--;
                index = tempIndex;
            }
        } catch (IndexOutOfBoundsException e) {
            throw new NotEnoughSpaceException("Parcels doesn't fit given trucks");
        }
    }

    /**
     * Метод проверки уровня в кузове на полную загруженность.
     *
     * @param layerLevel уровень в кузове, который нужно проверить.
     * @return {@code true} если на уровне остались свободные ячейки, {@code false} если все ячейки заняты
     */
    public boolean isLayerAvailable(int layerLevel) {
        char[] layer = getLayer(layerLevel);

        for (char ch : layer) {
            if (ch == EMPTY_SPACE_DESIGNATION)
                return true;
        }
        return false;
    }

    /**
     * Метод конвертации кузова грузовика в строку.
     * @return строку, представляющую кузов.
     */
    public String convertToPrettyBody() {
        StringBuilder sb = new StringBuilder();

        for (char[] layer : body) {
            sb.append("+");
            for (char ch : layer) {
                sb.append(ch != EMPTY_SPACE_DESIGNATION ? ch : " ");
            }
            sb.append("+\n");
        }
        sb.append("+".repeat(width)).append("++");

        return sb.toString();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Truck truck = (Truck) o;
        return Objects.deepEquals(body, truck.body);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(body);
    }
}
