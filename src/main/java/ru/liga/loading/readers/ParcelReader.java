package ru.liga.loading.readers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.liga.loading.models.Parcel;
import ru.liga.loading.serializers.ParcelSerializer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

@Slf4j
@RequiredArgsConstructor
@Component
public class ParcelReader {

    private final ParcelSerializer parcelSerializer;

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

    /**
     * Метод чтения формы посылки из консоли.
     * @param name имя посылки.
     * @param symbol символ посылки.
     * @return посылку с заполненной формой.
     */
    public Parcel readParcel(String name, char symbol) {
        char[][] box = readParcelBox();
        return new Parcel(name, symbol, box);
    }

    private char[][] readParcelBox() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter parcel height:");
        int height = scanner.nextInt();

        char[][] box = new char[height][];
        String line = null;

        scanner.nextLine();
        for (int i = height - 1; i >= 0; i--) {
            System.out.printf("Enter layer %d:%n", height - i);
            line = scanner.nextLine();
            box[i] = line.toCharArray();
        }
        return box;
    }
}
