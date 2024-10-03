package ru.liga.loading.view;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.liga.loading.models.Truck;
import ru.liga.loading.utils.TruckUtils;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class TruckView {

    private final TruckUtils truckUtils;

    /**
     * Метод отображения кузовов грузовиков.
     * @param trucks список грузовиков.
     */
    public void displayTrucks(List<Truck> trucks) {
        log.trace("Displaying truck bodies on screen");
        for (Truck truck : trucks) {
            truck.printBody();
            System.out.println();
        }
    }

    /**
     * Метод отображения кузовов грузовиков, посылок и их количества.
     * @param trucks список грузовиков.
     */
    public void displayParcelAmountAndTruckBodies(List<Truck> trucks) {
        log.trace("Displaying amount of parcels by rate and truck bodies on screen");
        for (Truck truck : trucks) {
            Map<Character, Integer> countedParcels = truckUtils.countParcels(truck);

            for (Map.Entry<Character, Integer> entry : countedParcels.entrySet()) {
                System.out.println("Amount of parcels with symbol " + entry.getKey() + " - " + entry.getValue());
            }
            System.out.println("Truck's body");
            truck.printBody();
        }
    }
}
