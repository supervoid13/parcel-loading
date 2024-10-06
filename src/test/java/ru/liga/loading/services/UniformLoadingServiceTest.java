package ru.liga.loading.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import ru.liga.loading.exceptions.NotEnoughParcelsException;
import ru.liga.loading.exceptions.NotEnoughTrucksException;
import ru.liga.loading.models.Parcel;
import ru.liga.loading.models.Truck;
import ru.liga.loading.utils.LoadingUtils;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(properties = "spring.shell.interactive.enabled=false")
public class UniformLoadingServiceTest {

    private static final int DEFAULT_WIDTH = 6;
    private static final int DEFAULT_HEIGHT = 6;

    private final LoadingService loadingService;

    @Autowired
    public UniformLoadingServiceTest(@Qualifier("uniform") LoadingService loadingService) {
        this.loadingService = loadingService;
    }

    @Test
    public void loadTrucksWithParcelsWithGivenTrucks_givenTooMuchParcels_shouldThrowNotEnoughTrucksException() {
        List<Parcel> parcels = getTooMuchParcels();
        List<Truck> trucks = LoadingUtils.generateEmptyTrucks(2, DEFAULT_WIDTH, DEFAULT_HEIGHT);

        assertThatThrownBy(() -> loadingService.loadTrucksWithParcelsWithGivenTrucks(parcels, trucks))
                .isInstanceOf(NotEnoughTrucksException.class);
    }

    @Test
    public void loadTrucksWithParcelsWithGivenTrucks_givenTooMuchTrucks_shouldThrowNotEnoughParcelsException() {
        List<Parcel> parcels = List.of(new Parcel(new char[][]{{'1'}}));
        List<Truck> trucks = LoadingUtils.generateEmptyTrucks(2, DEFAULT_WIDTH, DEFAULT_HEIGHT);

        assertThatThrownBy(() -> loadingService.loadTrucksWithParcelsWithGivenTrucks(parcels, trucks))
                .isInstanceOf(NotEnoughParcelsException.class);
    }

    @Test
    public void loadTrucksWithParcelsWithGivenTrucks_givenValidNumberOfParcels_shouldLoadTrucksUniform() {
        List<Parcel> parcels = getValidNumberOfParcels();
        List<Truck> trucks = LoadingUtils.generateEmptyTrucks(4, DEFAULT_WIDTH, DEFAULT_HEIGHT);

        List<Truck> actualLoadedTrucks = loadingService.loadTrucksWithParcelsWithGivenTrucks(parcels, trucks);
        List<Truck> expectedLoadedTrucks = getExpectedLoadedTrucks();

        assertThat(actualLoadedTrucks).isEqualTo(expectedLoadedTrucks);
    }

    private List<Truck> getExpectedLoadedTrucks() {
        char o = Truck.EMPTY_SPACE_DESIGNATION;

        return List.of(
                new Truck(new char[][] {
                        {o, o, o, o, o, o},
                        {o, o, o, o, o, o},
                        {o, o, o, o, o, o},
                        {o, o, o, o, o, o},
                        {'6', '6', '6', o, o, o},
                        {'6', '6', '6', o, o, o},
                }),
                new Truck(new char[][] {
                        {o, o, o, o, o, o},
                        {o, o, o, o, o, o},
                        {o, o, o, o, o, o},
                        {o, o, o, o, o, o},
                        {o, o, o, o, o, o},
                        {'5', '5', '5', '5', '5', '1'},
                }),
                new Truck(new char[][] {
                        {o, o, o, o, o, o},
                        {o, o, o, o, o, o},
                        {o, o, o, o, o, o},
                        {'9', '9', '9', o, o, o},
                        {'9', '9', '9', o, o, o},
                        {'9', '9', '9', o, o, o},
                }),
                new Truck(new char[][] {
                        {o, o, o, o, o, o},
                        {o, o, o, o, o, o},
                        {o, o, o, o, o, o},
                        {o, o, o, o, o, o},
                        {o, o, o, o, o, o},
                        {'3', '3', '3', '1', o, o},
                })
        );
    }

    private List<Parcel> getValidNumberOfParcels() {
        return List.of(
                new Parcel(new char[][]{{'1'}}),
                new Parcel(new char[][]{{'1'}}),
                new Parcel(new char[][]{{'3', '3', '3'}}),
                new Parcel(new char[][]{{'5', '5', '5', '5', '5'}}),
                new Parcel(new char[][]{
                        {'6', '6', '6'},
                        {'6', '6', '6'}
                }),
                new Parcel(new char[][]{
                        {'9', '9', '9'},
                        {'9', '9', '9'},
                        {'9', '9', '9'}
                })
        );
    }

    private List<Parcel> getTooMuchParcels() {
        return Stream.generate(() -> new Parcel(new char[][]{
                        {'9', '9', '9'},
                        {'9', '9', '9'},
                        {'9', '9', '9'}
                }))
                .limit(10)
                .toList();
    }
}
