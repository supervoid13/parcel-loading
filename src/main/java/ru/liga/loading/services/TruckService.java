package ru.liga.loading.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.liga.loading.models.Truck;
import ru.liga.loading.view.TruckView;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TruckService {

    private final TruckView truckView;


    /**
     * Метод отображения кузовов грузовиков.
     * @param trucks список грузовиков.
     */
    public void displayTrucks(List<Truck> trucks) {
        truckView.displayTrucks(trucks);
    }

    /**
     * Метод отображения кузовов грузовиков, посылок и их количества.
     * @param trucks список грузовиков.
     */
    public void displayParcelAmountAndTruckBodies(List<Truck> trucks) {
        truckView.displayParcelAmountAndTruckBodies(trucks);
    }
}
