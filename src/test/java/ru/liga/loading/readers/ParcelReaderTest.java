package ru.liga.loading.readers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.liga.loading.models.Parcel;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "spring.shell.interactive.enabled=false")
public class ParcelReaderTest {

    private final ParcelReader parcelReader;

    @Autowired
    public ParcelReaderTest(ParcelReader parcelReader) {
        this.parcelReader = parcelReader;
    }

    @Test
    public void readParcelsFromFile_givenEmptyFile_shouldReturnEmptyList() throws IOException {
        URL filePath = getClass().getClassLoader().getResource("empty_file.txt");
        List<Parcel> parcels = parcelReader.readParcelsFromFile(filePath.getPath());

        assertThat(parcels).isEmpty();
    }

    @Test
    public void readParcelsFromFile_givenValidFile_shouldReturnCorrectParcelList() throws IOException {
        URL filePath = getClass().getClassLoader().getResource("parcel_service_test_1.txt");
        List<Parcel> parcels = parcelReader.readParcelsFromFile(filePath.getPath());

        List<Parcel> expectedParcels = getParcels();

        assertThat(parcels).isEqualTo(expectedParcels);
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
