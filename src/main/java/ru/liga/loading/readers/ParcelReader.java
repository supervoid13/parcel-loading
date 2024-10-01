package ru.liga.loading.readers;

import lombok.extern.slf4j.Slf4j;
import ru.liga.loading.models.Parcel;
import ru.liga.loading.serializers.ParcelSerializer;
import ru.liga.loading.validators.ParcelValidator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

@Slf4j
public class ParcelReader {

    private final ParcelSerializer parcelSerializer = new ParcelSerializer();

    /**
     * Метод чтения посылок из файла.
     * @param filePath путь к файлу.
     * @return список посылок
     * @throws IOException если при чтении из файла произошла ошибка ввода-вывода или была прочитана
     * некорректная или неотображаемая последовательность байтов
     */
    public List<Parcel> readParcelsFromFile(String filePath) throws IOException {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.debug("Method '%s' has started".formatted(methodName));

        String text = Files.readString(Path.of(filePath));
        if (text.isBlank()) {
            log.warn("File is empty");
            return Collections.emptyList();
        }
        List<Parcel> parcels = parcelSerializer.deserializeList(text);

        log.debug("Method '%s' has finished".formatted(methodName));
        return parcels;
    }
}
