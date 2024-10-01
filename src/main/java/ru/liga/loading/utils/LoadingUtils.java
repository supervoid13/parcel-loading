package ru.liga.loading.utils;

import lombok.extern.slf4j.Slf4j;
import ru.liga.loading.exceptions.NotEnoughTrucksException;
import ru.liga.loading.models.Truck;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
public class LoadingUtils {

    /**
     * Метод копирования двумерного массива.
     * @param array исходный массив.
     * @return копию двумерного массива.
     */
    public static char[][] getArrayCopy(char[][] array) {
        char[][] copy = new char[array.length][];

        for (int i = 0; i < array.length; i++) {
            copy[i] = Arrays.copyOf(array[i], array[i].length);
        }

        return copy;
    }

    /**
     * Метод генерирования пустых грузовиков заданного количества.
     * @param trucksAmount количество грузовиков.
     * @return список пустых грузовиков.
     */
    public static List<Truck> generateEmptyTrucks(int trucksAmount, int width, int height) {
        if (trucksAmount < 1) throw new NotEnoughTrucksException("Amount of trucks must be 1 or greater");
        try {
            List<Truck> trucks = Stream.generate(() -> new Truck(width, height))
                    .limit(trucksAmount)
                    .toList();
            log.info("Amount of trucks - %d".formatted(trucksAmount));
            return trucks;
        } catch (IllegalArgumentException e) {
            log.error("Invalid amount of trucks");
            throw new IllegalArgumentException("Invalid amount of trucks");
        }
    }
}
