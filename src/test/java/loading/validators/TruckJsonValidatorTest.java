package loading.validators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.liga.loading.exceptions.TruckValidationException;
import ru.liga.loading.models.Truck;
import ru.liga.loading.validators.TruckJsonValidator;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TruckJsonValidatorTest {

    private TruckJsonValidator truckJsonValidator;

    @BeforeEach
    public void setup() {
        truckJsonValidator = new TruckJsonValidator();
    }

    @Test
    public void validate_givenInvalidInput_shouldThrowParcelValidationException() {
        char o = Truck.EMPTY_SPACE_DESIGNATION;

        char[][] body = new char[][] {
                {o, o, o, o, o, o},
                {o, o, o, o, o, o},
                {'8', '7', '7', '7', o, o},
                {'8', '8', '8', '8', o, o},
                {'8', '8', '8', '8', '2', '2'},
                {'5', '5', '5', '5', '5', '1'},
        };

        assertThatThrownBy(() -> truckJsonValidator.validate(body))
                .isInstanceOf(TruckValidationException.class);
    }

    @Test
    public void validate_givenValidInput_shouldExecuteWithoutExceptions() {
        char o = Truck.EMPTY_SPACE_DESIGNATION;

        char[][] body = new char[][] {
                {'4', '4', '4', '4', o, o},
                {'7', '7', '7', o, o, o},
                {'7', '7', '7', '7', o, o},
                {'8', '8', '8', '8', o, o},
                {'8', '8', '8', '8', '2', '2'},
                {'5', '5', '5', '5', '5', '1'},
        };

        assertThatCode(() -> truckJsonValidator.validate(body)).doesNotThrowAnyException();
    }
}
