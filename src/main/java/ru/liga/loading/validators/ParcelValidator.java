package ru.liga.loading.validators;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.liga.loading.exceptions.ParcelValidationException;
import ru.liga.loading.models.Parcel;
import ru.liga.loading.repositories.ParcelRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ParcelValidator {

    private final ParcelRepository parcelRepository;

    /**
     * Валидация посылки.
     * @param parcel строка со снимком посылки.
     * @throws ParcelValidationException если снимок посылки невалиден.
     */
    public void validateExisting(Parcel parcel) {
        char symbol = parcel.getBox()[0][0];
        Optional<Parcel> parcelOpt = parcelRepository.findBySymbol(symbol);

        parcelOpt.orElseThrow(() -> {
            log.error("Parcels are not valid");
            return new NoSuchElementException("Parcel not found");
        });
    }

    /**
     * Метод проверки валидности посылки.
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

    /**
     * Метод для валидации посылки на существование и соответствие формы.
     * @param parcel посылка.
     */
    public void validate(Parcel parcel) {
        System.out.println("validating " + parcel);
        validateExisting(parcel);
        validateBox(parcel);
    }
}
