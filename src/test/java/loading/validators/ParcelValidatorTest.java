package loading.validators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.liga.loading.exceptions.ParcelValidationException;
import ru.liga.loading.validators.ParcelValidator;

import static org.assertj.core.api.Assertions.*;

public class ParcelValidatorTest {

    private ParcelValidator parcelValidator;

    @BeforeEach
    public void setup() {
        parcelValidator = new ParcelValidator();
    }

    @Test
    public void validate_givenInvalidInput_shouldThrowParcelValidationException() {
        String text = "555";
        assertThatThrownBy(() -> parcelValidator.validate(text))
                .isInstanceOf(ParcelValidationException.class);
    }

    @Test
    public void validate_givenValidInput_shouldExecuteWithoutExceptions() {
        String text = "999\n999\n999";
        assertThatCode(() -> parcelValidator.validate(text)).doesNotThrowAnyException();
    }
}
