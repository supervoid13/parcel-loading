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
     */
    public List<Parcel> readParcelsFromFile(String filePath) {
        log.debug("Method '{}' has started", "readParcelsFromFile");

        List<Parcel> parcels;
        try {
            String text = Files.readString(Path.of(filePath));

            if (text.isBlank()) {
                return Collections.emptyList();
            }
            parcels = parcelSerializer.deserializeList(text);
        } catch (IOException e) {
            log.error("Problem with reading parcels from file");
            return Collections.emptyList();
        }

        log.debug("Method '{}' has finished", "readParcelsFromFile");
        return parcels;
    }

    /**
     * Метод чтения формы посылки из консоли.
     * @param name имя посылки.
     * @param symbol символ посылки.
     * @return посылку с заполненной формой.
     */
    public Parcel readParcelFromConsole(String name, char symbol) {
        char[][] box = readParcelBoxFromConsole();
        return new Parcel(name, symbol, box);
    }

    public Parcel readParcelFromFile(String name, char symbol, String filePath) {
        char[][] box = readParcelBoxFromFile(filePath);
        return new Parcel(name, symbol, box);
    }

    private char[][] readParcelBoxFromFile(String filePath) {
        String text;
        try {
            text = Files.readString(Path.of(filePath));
        } catch (IOException e) {
            log.error("Problem with reading parcel box from file");
            return new char[][]{};
        }
        String[] layers = text.split("\\n");
        char[][] box = new char[layers.length][];

        for (int i = 0; i < layers.length; i++) {
            box[i] = layers[i].toCharArray();
        }
        return box;
    }

    private char[][] readParcelBoxFromConsole() {
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
