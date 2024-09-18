package ru.liga.service;

import ru.liga.model.Parcel;
import ru.liga.utils.ParcelValidator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ParcelService {

    private final ParcelValidator parcelValidator = new ParcelValidator();

    public List<Parcel> readParcelsFromFile(String filePath) throws IOException {
        List<Parcel> parcels = new ArrayList<>();

        String text = Files.readString(Path.of(filePath));

        if (text.isBlank())
            return Collections.emptyList();

        String[] parcelsStrArray = text.split("\\n{2,}");

        for (String parcelStr: parcelsStrArray) {
            parcelValidator.validate(parcelStr);

            Parcel parcel = Parcel.fromStr(parcelStr);
            parcels.add(parcel);
        }


        return parcels;
    }

    public List<Parcel> prepareParcels(List<Parcel> parcels) {
        ArrayList<Parcel> parcelsCopy = new ArrayList<>(parcels);

        parcelsCopy.sort(Comparator.comparing(Parcel::getBottomWidth)
                .thenComparing(Parcel::getSquare)
                .reversed());

        return parcelsCopy;
    }

    public double calculateAverageSquare(List<Parcel> parcels) {
        return parcels.stream()
                .map(Parcel::getSquare)
                .mapToInt(x -> x)
                .average()
                .orElse(0d);
    }
}
