package ru.liga.utils;

import ru.liga.exceptions.ParcelValidationException;

import java.util.Set;

public class ParcelValidator {

    private final Set<String> TEMPLATES = Set.of(
            "1",
            "22",
            "333",
            "4444",
            "55555",
            "666\n666",
            "777\n7777",
            "8888\n8888",
            "999\n999\n999"
    );

    public void validate(String parcelStr) {
        if (!TEMPLATES.contains(parcelStr))
            throw new ParcelValidationException("Not valid parcel string");
    }
}
