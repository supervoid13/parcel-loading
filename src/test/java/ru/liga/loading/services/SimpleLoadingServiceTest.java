package ru.liga.loading.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import ru.liga.loading.models.Parcel;
import ru.liga.loading.models.Truck;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "spring.shell.interactive.enabled=false")
public class SimpleLoadingServiceTest {

    private static final int DEFAULT_WIDTH = 6;
    private static final int DEFAULT_HEIGHT = 6;

    private final LoadingService loadingService;

    @Autowired
    public SimpleLoadingServiceTest(@Qualifier("simple") LoadingService loadingService) {
        this.loadingService = loadingService;
    }

    @Test
    public void loadTrucksWithParcels_givenParcelList_shouldReturnLoadedTruckList() {
        List<Truck> actualTrucks = loadingService.loadTrucksWithParcelsWithInfiniteTrucksAmount(
                getParcels(),
                DEFAULT_WIDTH,
                DEFAULT_HEIGHT
        );
        List<Truck> expectedTrucks = getTrucks();

        assertThat(actualTrucks).isEqualTo(expectedTrucks);
    }

    private List<Parcel> getParcels() {
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

    private List<Truck> getTrucks() {
        char o = Truck.EMPTY_SPACE_DESIGNATION;

        return List.of(
                new Truck(new char[][] {
                        {o, o, o, o, o, o},
                        {o, o, o, o, o, o},
                        {o, o, o, o, o, o},
                        {'9', '9', '9', o, o, o},
                        {'9', '9', '9', o, o, o},
                        {'9', '9', '9', o, o, o}
                }),
                new Truck(new char[][] {
                        {o, o, o, o, o, o},
                        {o, o, o, o, o, o},
                        {o, o, o, o, o, o},
                        {o, o, o, o, o, o},
                        {'6', '6', '6', o, o, o},
                        {'6', '6', '6', o, o, o}
                }),
                new Truck(new char[][] {
                        {o, o, o, o, o, o},
                        {o, o, o, o, o, o},
                        {o, o, o, o, o, o},
                        {o, o, o, o, o, o},
                        {o, o, o, o, o, o},
                        {'5', '5', '5', '5', '5', o}
                }),
                new Truck(new char[][] {
                        {o, o, o, o, o, o},
                        {o, o, o, o, o, o},
                        {o, o, o, o, o, o},
                        {o, o, o, o, o, o},
                        {o, o, o, o, o, o},
                        {'3', '3', '3', o, o, o}
                }),
                new Truck(new char[][] {
                        {o, o, o, o, o, o},
                        {o, o, o, o, o, o},
                        {o, o, o, o, o, o},
                        {o, o, o, o, o, o},
                        {o, o, o, o, o, o},
                        {'1', o, o, o, o, o}
                }),
                new Truck(new char[][] {
                        {o, o, o, o, o, o},
                        {o, o, o, o, o, o},
                        {o, o, o, o, o, o},
                        {o, o, o, o, o, o},
                        {o, o, o, o, o, o},
                        {'1', o, o, o, o, o}
                })
        );
    }
}
