package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.liga.model.Parcel;
import ru.liga.service.ParcelService;

import java.io.IOException;
import java.util.List;

public class ParcelServiceTest {

    private ParcelService parcelService;

    @BeforeEach
    public void setup() {
        parcelService = new ParcelService();
    }

    @Test
    public void shouldReturnEmptyList() throws IOException {
        List<Parcel> parcels = parcelService.readParcelsFromFile("empty_file.txt");

        Assertions.assertTrue(parcels.isEmpty());
    }

    @Test
    public void shouldReadParcelsFromFile() throws IOException {
        List<Parcel> parcels = parcelService.readParcelsFromFile("parcel_service_test_1.txt");

        List<Parcel> expectedParcels = getParcels();

        Assertions.assertIterableEquals(expectedParcels, parcels);
    }

    private List<Parcel> getParcels() {
        return List.of(
                new Parcel(new char[][]{{'1'}}),
                new Parcel(new char[][]{{'2', '2'}}),
                new Parcel(new char[][]{{'3', '3', '3'}}),
                new Parcel(new char[][]{{'4', '4', '4', '4'}}),
                new Parcel(new char[][]{{'5', '5', '5', '5', '5'}}),
                new Parcel(new char[][]{
                        {'6', '6', '6'},
                        {'6', '6', '6'}
                }),
                new Parcel(new char[][]{
                        {'7', '7', '7'},
                        {'7', '7', '7', '7'}
                }),
                new Parcel(new char[][]{
                        {'8', '8', '8', '8'},
                        {'8', '8', '8', '8'}
                }),
                new Parcel(new char[][]{
                        {'9', '9', '9'},
                        {'9', '9', '9'},
                        {'9', '9', '9'}
                })
        );
    }
}
