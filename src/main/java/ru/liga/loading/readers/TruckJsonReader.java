package ru.liga.loading.readers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.liga.loading.models.Truck;
import ru.liga.loading.serializers.TruckSerializer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class TruckJsonReader {

    private final TruckSerializer truckSerializer;

    /**
     * Метод чтения грузовиков из json файла.
     * @param filePath путь к json файлу.
     * @return список грузовиков.
     */
    public List<Truck> readTrucksFromJson(String filePath) {
        log.debug("Method '{}' has started", "readTrucksFromJson");

        List<Truck> trucks;
        try {
            String json = Files.readString(Path.of(filePath));
            trucks = truckSerializer.deserialize(json);
        } catch (IOException e) {
            log.error("Problem with reading trucks from file");
            return Collections.emptyList();
        }

        log.debug("Method '{}' has finished", "readTrucksFromJson");
        return trucks;
    }
}
