package ru.liga.loading.models;

import ru.liga.loading.utils.LoadingUtils;

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
    private final static int CHAR_TO_DIGIT_DIFF = 48;

    private final char[][] body;

    public Truck() {
        body = new char[HEIGHT_CAPACITY][WIDTH_CAPACITY];
    }

    public Truck(char[][] body) {
        this.body = LoadingUtils.getArrayCopy(body);
    }

    public char[][] getBody() {
        return LoadingUtils.getArrayCopy(body);
    }

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

    public int getEmptySpaceSquare() {
        return BODY_SQUARE - getOccupiedSpaceSquare();
    }

    private char[] getLayer(int layerLevel) {
        return body[HEIGHT_CAPACITY - layerLevel];
    }

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

    public boolean tryLoadParcel(Parcel parcel, int layerLevel, int spaceWidth, int index) {
        int tempIndex = index;
        int parcelBottomWidth = parcel.getBottomWidth();

        if (spaceWidth < parcelBottomWidth || !isBottomLayerValid(parcelBottomWidth, layerLevel - 1, index)) {
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

    public boolean isBottomLayerValid(int parcelBottomWidth, int bottomLayerLevel, int index) {
        if (bottomLayerLevel == 0) return true;

        int supportWidthCounter = 0;
        char[] layer = getLayer(bottomLayerLevel);

        for (int i = index; i < WIDTH_CAPACITY; i++) {
            if (layer[i] != EMPTY_SPACE_DESIGNATION)
                supportWidthCounter++;
        }

        return supportWidthCounter > parcelBottomWidth / 2;
    }

    public boolean isLayerAvailable(int layerLevel) {
        char[] layer = getLayer(layerLevel);

        for (char ch : layer) {
            if (ch == EMPTY_SPACE_DESIGNATION)
                return true;
        }
        return false;
    }

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
                        entry -> entry.getValue() / squareFromParcelChar(entry.getKey())
                ));
    }

    private int squareFromParcelChar(char parcelChar) {
        return parcelChar - CHAR_TO_DIGIT_DIFF;
    }

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
