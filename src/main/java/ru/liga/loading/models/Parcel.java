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

    /**
     * Получение объекта-посылки из строки
     * @param parcelStr строка, содержащая внутри себя снимок тела посылки
     * @return объект-посылка
     */
    public static Parcel fromStr(String parcelStr) {
        int height = (int) parcelStr.chars().filter(ch -> ch == '\n').count() + 1;

        char[][] box = new char[height][];

        int currentLayer = 1;

        List<Character> charsOnLayer = new ArrayList<>();

        for (int i = parcelStr.length() - 1; i >= 0; i--) {
            if (parcelStr.charAt(i) != '\n') {
                charsOnLayer.add(parcelStr.charAt(i));
                if (i != 0)
                    continue;
            }
            box[height - currentLayer] = charsOnLayer.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining())
                    .toCharArray();

            charsOnLayer = new ArrayList<>();
            currentLayer++;
        }

        return new Parcel(box);
    }
}
