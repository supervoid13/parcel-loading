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


    public String convertTrucksToPrettyOutput(List<Truck> trucks) {
        log.trace("Converting trucks to pretty output of amount of parcels by rate and truck bodies");

        StringBuilder sb = new StringBuilder();

        for (Truck truck : trucks) {
            Map<Character, Integer> countedParcels = truckUtils.countParcels(truck);

            for (Map.Entry<Character, Integer> entry : countedParcels.entrySet()) {
                sb.append("Amount of parcels with symbol ").append(entry.getKey()).append(" - ").append(entry.getValue()).append("\n");
            }
            sb.append("Truck's body\n");
            sb.append(truck.convertToPrettyBody()).append("\n");
        }

        return sb.toString();
    }
}
