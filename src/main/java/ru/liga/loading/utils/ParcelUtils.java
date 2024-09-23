package ru.liga.loading.utils;

import ru.liga.loading.models.Parcel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ParcelUtils {

    private final static int CHAR_TO_DIGIT_DIFF = 48;

    /**
     * Метод сортировки списка посылок по ширине основания и площади в обратном порядке.
     * @param parcels список посылок
     * @return отсортированную копию списка посылок.
     */
    public List<Parcel> prepareParcelsByBottomWidthThenSquare(List<Parcel> parcels) {
        ArrayList<Parcel> parcelsCopy = new ArrayList<>(parcels);

        parcelsCopy.sort(Comparator.comparing(Parcel::getBottomWidth)
                .thenComparing(Parcel::getSquare)
                .reversed());

        return parcelsCopy;
    }

    /**
     * Метод сортировки списка посылок по площади в обратном порядке.
     * @param parcels список посылок.
     * @return отсортированную копию списка посылок.
     */
    public List<Parcel> prepareParcelsBySquare(List<Parcel> parcels) {
        ArrayList<Parcel> parcelsCopy = new ArrayList<>(parcels);

        parcelsCopy.sort(Comparator.comparing(Parcel::getSquare)
                .reversed());

        return parcelsCopy;
    }

    /**
     * Метод подсчёта площади посылки по её символьному идентификатору.
     * @param parcelChar символьный идентификатор.
     * @return площадь посылки.
     */
    public static int squareFromParcelChar(char parcelChar) {
        return parcelChar - CHAR_TO_DIGIT_DIFF;
    }
}
