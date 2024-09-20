package loading.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.liga.loading.models.Parcel;
import ru.liga.loading.models.Truck;
import ru.liga.loading.services.LoadingService;
import ru.liga.loading.services.SimpleLoadingService;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class SimpleLoadingServiceTest {

    private LoadingService loadingService;

    @BeforeEach
    public void setup() {
        loadingService = new SimpleLoadingService();
    }

    @Test
    public void loadTrucksWithParcels_givenParcelList_shouldReturnLoadedTruckList() {
        List<Truck> actualTrucks = loadingService.loadTrucksWithParcelsWithInfiniteTrucksAmount(getParcels());
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
