package ru.liga.loading.validators;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.liga.loading.exceptions.TruckValidationException;
import ru.liga.loading.models.Truck;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(properties = "spring.shell.interactive.enabled=false")
public class TruckValidatorTest {

    private final TruckValidator truckValidator;

    @Autowired
    public TruckValidatorTest(TruckValidator truckValidator) {
        this.truckValidator = truckValidator;
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
        List<Truck> trucks = List.of(new Truck(body));

        assertThatThrownBy(() -> truckValidator.validateTruckList(trucks))
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
        List<Truck> trucks = List.of(new Truck(body));

        assertThatCode(() -> truckValidator.validateTruckList(trucks)).doesNotThrowAnyException();
    }
}
