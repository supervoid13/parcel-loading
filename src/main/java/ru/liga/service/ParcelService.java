package ru.liga.service;

import ru.liga.model.Parcel;
import ru.liga.utils.ParcelValidator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

            Parcel parcel = Parcel.FromStr(parcelStr);
            parcels.add(parcel);
        }


        return parcels;
    }


}
