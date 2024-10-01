package ru.liga.loading.validators;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.liga.loading.exceptions.ParcelNotFound;
import ru.liga.loading.exceptions.ParcelValidationException;
import ru.liga.loading.models.Parcel;
import ru.liga.loading.repositories.ParcelRepository;

import java.io.FileNotFoundException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ParcelValidator {

    private final ParcelRepository parcelRepository;

    /**
     * Валидация посылки.
     * @param parcel строка со снимком посылки.
     * @throws ParcelValidationException если снимок посылки невалиден.
     * @throws FileNotFoundException если не найден файл с посылками.
     */
    public void validateExisting(Parcel parcel) throws FileNotFoundException {
        List<Parcel> existingParcels = parcelRepository.findAll();
        if (!existingParcels.contains(parcel)) {
            log.error("Parcels are not valid");
            throw new ParcelNotFound("Parcel not found");
        }
    }

    /**
     * Метод проверки соответствия формы
     * @param parcel посылка.
     * @throws ParcelValidationException если не все символы в форме посылки соответствуют её символу.
     */
    public void validateBox(Parcel parcel) {
        char[][] box = parcel.getBox();
        char parcelSymbol = parcel.getSymbol();

        for (char[] layer : box) {
            for (char sym : layer) {
                if (sym != parcelSymbol && sym != ' ')
                    throw new ParcelValidationException("All box symbols must be parcel symbol");
            }
        }
    }
}
