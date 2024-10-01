package ru.liga.loading.readers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.liga.loading.models.Truck;
import ru.liga.loading.serializers.TruckSerializer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
     * @throws IOException если при чтении из файла произошла ошибка ввода-вывода или была прочитана
     * некорректная или неотображаемая последовательность байтов
     */
    public List<Truck> readTrucksFromJson(String filePath) throws IOException {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.debug("Method '%s' has started".formatted(methodName));

        String json = Files.readString(Path.of(filePath));
        List<Truck> trucks = truckSerializer.deserialize(json);

        log.debug("Method '%s' has finished".formatted(methodName));
        return trucks;
    }
}
