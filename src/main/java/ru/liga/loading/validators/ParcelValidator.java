package ru.liga.loading.validators;

import lombok.extern.slf4j.Slf4j;
import ru.liga.loading.exceptions.ParcelValidationException;

import java.util.Set;

@Slf4j
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

    /**
     * Валидация строки, содержащей снимок посылки.
     * @param parcelStr строка со снимком посылки.
     * @throws ParcelValidationException если снимок посылки невалиден.
     */
    public void validate(String parcelStr) {
        if (!TEMPLATES.contains(parcelStr)) {
            log.error("Parcels are not valid");
            throw new ParcelValidationException("Not valid parcel string");
        }
    }
}
