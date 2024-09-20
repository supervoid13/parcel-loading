package ru.liga.loading.utils;

import ru.liga.loading.models.Parcel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ParcelUtils {

    public List<Parcel> prepareParcelsByBottomWidthThenSquare(List<Parcel> parcels) {
        ArrayList<Parcel> parcelsCopy = new ArrayList<>(parcels);

        parcelsCopy.sort(Comparator.comparing(Parcel::getBottomWidth)
                .thenComparing(Parcel::getSquare)
                .reversed());

        return parcelsCopy;
    }

    public List<Parcel> prepareParcelsBySquare(List<Parcel> parcels) {
        ArrayList<Parcel> parcelsCopy = new ArrayList<>(parcels);

        parcelsCopy.sort(Comparator.comparing(Parcel::getSquare)
                .reversed());

        return parcelsCopy;
    }
}
