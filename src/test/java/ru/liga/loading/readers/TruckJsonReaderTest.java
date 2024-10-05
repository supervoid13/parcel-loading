package ru.liga.loading.readers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.liga.loading.exceptions.TruckValidationException;
import ru.liga.loading.models.Truck;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(properties = "spring.shell.interactive.enabled=false")
public class TruckJsonReaderTest {

    private final TruckJsonReader truckJsonReader;

    @Autowired
    public TruckJsonReaderTest(TruckJsonReader truckJsonReader) {
        this.truckJsonReader = truckJsonReader;
    }

    @Test
    public void readTrucksFromJson_givenInvalidJson_shouldThrowTruckValidationException() {
        String filePath = getClass().getClassLoader().getResource("invalid_trucks.json").getPath();

        assertThatThrownBy(() -> truckJsonReader.readTrucksFromJson(filePath))
                .isInstanceOf(TruckValidationException.class);
    }

    @Test
    public void readTrucksFromJson_givenValidJson_shouldReturnCorrectTruckList() {
        String filePath = getClass().getClassLoader().getResource("valid_trucks.json").getPath();
        List<Truck> actualTrucks = truckJsonReader.readTrucksFromJson(filePath);
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
