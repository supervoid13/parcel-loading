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

        String json;
        try {
            json = Files.readString(Path.of(filePath));
        } catch (IOException e) {
            log.error("Problem with reading trucks from file");
            return Collections.emptyList();
        }
        List<Truck> trucks = readTrucksFromJsonString(json);

        log.debug("Method '{}' has finished", "readTrucksFromJson");
        return trucks;
    }

    /**
     * Метод для чтения грузовиков из json строки.
     * @param json json строка.
     * @return список грузовиков.
     */
    public List<Truck> readTrucksFromJsonString(String json) {
        return truckSerializer.deserialize(json);
    }
}
