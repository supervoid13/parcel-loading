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
     * Метод конвертации грузовиков с посылками в строку.
     * @param trucks грузовики.
     * @return удобно-читаемую строку.
     */
    public String getPrettyOutputForTrucks(List<Truck> trucks) {
        return truckView.convertTrucksToPrettyOutput(trucks);
    }
}
