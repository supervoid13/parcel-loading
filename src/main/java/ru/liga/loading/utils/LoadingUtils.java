package ru.liga.loading.utils;

import ru.liga.loading.models.Truck;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class LoadingUtils {

    public static char[][] getArrayCopy(char[][] array) {
        char[][] copy = new char[array.length][];

        for (int i = 0; i < array.length; i++) {
            copy[i] = Arrays.copyOf(array[i], array[i].length);
        }

        return copy;
    }

    public static List<Truck> generateEmptyTrucks(int trucksAmount) {
        return Stream.generate(Truck::new)
                .limit(trucksAmount)
                .toList();
    }
}
