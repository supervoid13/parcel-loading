package ru.liga.utils;

import java.util.Arrays;

public class LoadingUtils {

    public static char[][] getArrayCopy(char[][] array) {
        char[][] copy = new char[array.length][];

        for (int i = 0; i < array.length; i++) {
            copy[i] = Arrays.copyOf(array[i], array[i].length);
        }

        return copy;
    }
}
