package ru.liga.loading.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.liga.loading.models.Parcel;
import ru.liga.loading.repositories.ParcelRepository;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class ParcelUtils {

    private final static int CHAR_TO_DIGIT_DIFF = 48;

    private final ParcelRepository parcelRepository;

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

    public int calculateSquareFromSymbol(char symbol) {
        Parcel parcel;
        try {
            parcel = parcelRepository.findBySymbol(symbol).orElseThrow(
                    () -> new NoSuchElementException("No such parcel")
            );
        } catch (FileNotFoundException e) {
            throw new RuntimeException("No data about parcels");
        }

        int result = 0;
        char parcelSymbol = parcel.getSymbol();

        char[][] box = parcel.getBox();

        for (char[] layer : box) {
            for (char sym : layer) {
                if (sym == parcelSymbol)
                    result++;
            }
        }
        return result;
    }
}
