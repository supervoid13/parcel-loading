package loading.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.liga.loading.models.Parcel;
import ru.liga.loading.models.Truck;
import ru.liga.loading.services.EffectiveLoadingService;
import ru.liga.loading.services.LoadingService;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class EffectiveLoadingServiceTest {

    private LoadingService loadingService;

    @BeforeEach
    public void setup() {
        loadingService = new EffectiveLoadingService();
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

    private List<Truck> getTrucks() {
        char o = Truck.EMPTY_SPACE_DESIGNATION;

        return List.of(
                new Truck(new char[][] {
                        {'4', '4', '4', '4', o, o},
                        {'7', '7', '7', o, o, o},
                        {'7', '7', '7', '7', o, o},
                        {'8', '8', '8', '8', o, o},
                        {'8', '8', '8', '8', '2', '2'},
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
