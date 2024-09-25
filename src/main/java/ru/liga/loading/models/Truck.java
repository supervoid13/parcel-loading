package ru.liga.loading.models;

import ru.liga.loading.utils.LoadingUtils;
import ru.liga.loading.utils.ParcelUtils;
import ru.liga.loading.utils.TruckUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Truck {

    public static final int WIDTH_CAPACITY = 6;
    public static final int HEIGHT_CAPACITY = 6;
    public static final int BODY_SQUARE = WIDTH_CAPACITY * HEIGHT_CAPACITY;
    public static final char EMPTY_SPACE_DESIGNATION = '\u0000';

    private final char[][] body;

    /**
     * Конструктор - создание нового объекта и инициализация пустого кузова исходя из емкости.
     */
    public Truck() {
        body = new char[HEIGHT_CAPACITY][WIDTH_CAPACITY];
    }

    /**
     * Конструктор - создание нового объекта и инициализация кузова переданным кузовом.
     *
     * @param body кузов, с которым нужно создать объект грузовика.
     */
    public Truck(char[][] body) {
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
    public int getOccupiedSpaceSquare() {
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
    public int getEmptySpaceSquare() {
        return BODY_SQUARE - getOccupiedSpaceSquare();
    }

    public char[] getLayer(int layerLevel) {
        char[] layer = body[HEIGHT_CAPACITY - layerLevel];
        return Arrays.copyOf(layer, layer.length);
    }

    /**
     * Получение массива двух элементов: ширину свободного места и индекс его начала, соответственно,
     * на указанном уровне {@code layerLevel}.
     *
     * @param layerLevel уровень высоты в кузове. Начинается с 1.
     * @return массив двух элементов: ширина и индекс начала.
     */
    public int[] getEmptySpaceWidthAndIndexOnLayer(int layerLevel) {
        char[] layer = getLayer(layerLevel);

        int maxLength = 0;
        int maxIndex = -1;
        int currentLength = 0;
        int currentIndex = -1;

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
     * Метод попытки загрузить посылку {@code parcel} на указанный уровень {@code layerLevel} в свободное место
     * шириной {@code spaceWidth}, начиная с индекса {@code index}
     *
     * @param parcel     посылка, которую нужно загрузить.
     * @param layerLevel уровень, на который нужно загрузить посылку.
     * @param spaceWidth ширина свободного места на указанном уровне.
     * @param index      индекс, с которого нужно начать загрузку.
     * @return {@code true} если загрузка прошла успешно, {@code false} в противном случае.
     */
    public boolean tryLoadParcel(Parcel parcel, int layerLevel, int spaceWidth, int index) {
        int tempIndex = index;
        int parcelBottomWidth = parcel.getBottomWidth();

        if (spaceWidth < parcelBottomWidth
                || !TruckUtils.isBottomLayerValid(this, parcelBottomWidth, layerLevel - 1, index)) {
            return false;
        }

        char[][] box = parcel.getBox();
        int deepIndex = HEIGHT_CAPACITY - layerLevel;

        for (int i = box.length - 1; i >= 0; i--) {
            for (int j = 0; j < box[i].length; j++) {
                body[deepIndex][index++] = box[i][j];
            }
            deepIndex--;
            index = tempIndex;
        }
        return true;
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
     * Метод определения, какие посылки лежат в грузовике и их количества.
     *
     * @return {@code Map}, где {@code key} - символьный идентификатор посылки, {@code value} - количество
     * таких посылок
     */
    public Map<Character, Integer> countParcels() {
        Map<Character, Integer> result = new HashMap<>();

        for (char[] layer : body) {
            for (char ch : layer) {
                if (ch != Truck.EMPTY_SPACE_DESIGNATION)
                    result.put(ch, result.getOrDefault(ch, 0) + 1);
            }
        }

        return result.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue() / ParcelUtils.squareFromParcelChar(entry.getKey())
                ));
    }

    /**
     * Метод печати кузова грузовика {@link Truck#body} в удобно-читаемом виде
     */
    public void printBody() {
        StringBuilder sb = new StringBuilder();

        for (char[] layer : body) {
            sb.append("+");
            for (char ch : layer) {
                sb.append(ch != EMPTY_SPACE_DESIGNATION ? ch : " ");
            }
            sb.append("+\n");
        }
        sb.append("++++++++");

        System.out.println(sb);
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
