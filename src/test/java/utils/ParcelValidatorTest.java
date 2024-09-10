package utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.liga.exceptions.ParcelValidationException;
import ru.liga.utils.ParcelValidator;

public class ParcelValidatorTest {

    private ParcelValidator parcelValidator;

    @BeforeEach
    public void setup() {
        parcelValidator = new ParcelValidator();
    }

    @Test
    public void shouldThrowException() {
        String text = "555";
        Assertions.assertThrows(ParcelValidationException.class, () -> parcelValidator.validate(text));
    }

    @Test
    public void shouldWorkFine() {
        String text = "999\n999\n999";
        Assertions.assertDoesNotThrow(() -> parcelValidator.validate(text));
    }
}
