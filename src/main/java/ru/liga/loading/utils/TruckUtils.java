package ru.liga.loading.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.liga.loading.models.Truck;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TruckUtils {

    private final ParcelUtils parcelUtils;

    /**
     * Метод определения, какие посылки лежат в грузовике и их количества.
     *
     * @return {@code Map}, где {@code key} - символьный идентификатор посылки, {@code value} - количество
     * таких посылок
     */
    public Map<Character, Integer> countParcels(Truck truck) {
        Map<Character, Integer> result = new HashMap<>();

        for (char[] layer : truck.getBody()) {
            for (char ch : layer) {
                if (ch != Truck.EMPTY_SPACE_DESIGNATION)
                    result.put(ch, result.getOrDefault(ch, 0) + 1);
            }
        }

        return result.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue() / parcelUtils.calculateSquareFromSymbol(entry.getKey())
                ));
    }
}
