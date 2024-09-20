package ru.liga.loading.readers;

import lombok.extern.slf4j.Slf4j;
import ru.liga.loading.models.Parcel;
import ru.liga.loading.validators.ParcelValidator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class ParcelReader {

    private final ParcelValidator parcelValidator = new ParcelValidator();

    public List<Parcel> readParcelsFromFile(String filePath) throws IOException {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.debug("Method '%s' has started".formatted(methodName));

        List<Parcel> parcels = new ArrayList<>();

        String text = Files.readString(Path.of(filePath));

        if (text.isBlank()) {
            log.warn("File is empty");
            return Collections.emptyList();
        }

        String[] parcelsStrArray = text.split("\\n{2,}");

        log.info("Parcel validation has started");
        for (String parcelStr : parcelsStrArray) {
            parcelValidator.validate(parcelStr);

            Parcel parcel = Parcel.fromStr(parcelStr);
            parcels.add(parcel);
        }
        log.info("All parcels are valid");

        log.debug("Method '%s' has finished".formatted(methodName));
        return parcels;
    }
}
