package loading.readers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.liga.loading.exceptions.TruckValidationException;
import ru.liga.loading.models.Truck;
import ru.liga.loading.readers.TruckJsonReader;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class TruckJsonReaderTest {

    private TruckJsonReader truckJsonReader;

    @BeforeEach
    public void setup() {
        truckJsonReader = new TruckJsonReader();
    }

    @Test
    public void readParcelsFromJson_givenInvalidJson_shouldThrowTruckValidationException() {
        String filePath = getClass().getClassLoader().getResource("invalid_trucks.json").getPath();

        assertThatThrownBy(() -> truckJsonReader.readParcelsFromJson(filePath))
                .isInstanceOf(TruckValidationException.class);
    }

    @Test
    public void readParcelsFromJson_givenValidJson_shouldReturnCorrectTruckList() throws IOException {
        String filePath = getClass().getClassLoader().getResource("valid_trucks.json").getPath();
        List<Truck> actualTrucks = truckJsonReader.readParcelsFromJson(filePath);
        List<Truck> expectedTrucks = getTrucks();

        assertThat(actualTrucks).isEqualTo(expectedTrucks);
    }

    private List<Truck> getTrucks() {
        char o = Truck.EMPTY_SPACE_DESIGNATION;

        return List.of(
                new Truck(new char[][] {
                        {o, o, o, o, o, o},
                        {o, o, o, o, o, o},
                        {o, o, o, o, o, o},
                        {o, o, o, o, o, o},
                        {'2', '2', '2', '2', '2', '2'},
                        {'5', '5', '5', '5', '5', '1'},
                }),
                new Truck(new char[][] {
                        {o, o, o, o, o, o},
                        {o, o, o, o, o, o},
                        {o, o, o, o, o, o},
                        {'9', '9', '9', '3', '3', '3'},
                        {'9', '9', '9', '6', '6', '6'},
                        {'9', '9', '9', '6', '6', '6'},
                })
        );
    }
}
