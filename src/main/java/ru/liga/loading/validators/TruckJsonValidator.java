package ru.liga.loading.validators;

import lombok.extern.slf4j.Slf4j;
import ru.liga.loading.exceptions.TruckValidationException;
import ru.liga.loading.models.Truck;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
public class TruckJsonValidator {

    private final Map<Character, Integer> parcelCharWidth = Map.of(
            '1', 1,
            '2', 2,
            '3', 3,
            '4', 4,
            '5', 5,
            '6', 3,
            '7', 4,
            '8', 4,
            '9', 3
    );

    /**
     * Валидация кузова грузовика.
     * @param body кузов грузовика.
     * @throws TruckValidationException если кузов грузовика невалиден.
     */
    public void validate(char[][] body) {
        Set<String> validIndexes = new HashSet<>();

        for (int i = Truck.HEIGHT_CAPACITY - 1; i >= 0; i--) {
            for (int j = 0; j < Truck.WIDTH_CAPACITY; j++) {
                if (isCharAtIndexesAlreadyChecked(i, j, validIndexes)) continue;

                char parcelChar = body[i][j];

                if (parcelChar == Truck.EMPTY_SPACE_DESIGNATION) continue;

                int parcelWidth = parcelCharWidth.get(parcelChar);
                int parcelSquare = parcelChar - '0';

                int counter = 1, parcelLayer = Truck.HEIGHT_CAPACITY - i, parcelBodyIndexOnLayer = 1;

                while (counter < parcelSquare) {
                    if (parcelBodyIndexOnLayer == parcelWidth) {
                        parcelLayer++;
                        parcelBodyIndexOnLayer = 0;
                    }

                    try {
                        int toCheckI = Truck.HEIGHT_CAPACITY - parcelLayer, toCheckJ = parcelBodyIndexOnLayer + j;
                        char charToCheck = body[toCheckI][toCheckJ];

                        if (charToCheck != parcelChar) {
                            log.error("Invalid json file with trucks");
                            throw new TruckValidationException("Invalid truck json");
                        }

                        String indexesStr = (toCheckI) + "" + (toCheckJ);
                        validIndexes.add(indexesStr);
                        counter++;
                        parcelBodyIndexOnLayer++;

                    } catch (IndexOutOfBoundsException e) {
                        log.error("Invalid json file with trucks");
                        throw new TruckValidationException("Invalid truck json");
                    }
                }
            }
        }
    }

    private boolean isCharAtIndexesAlreadyChecked(int i, int j, Set<String> validIndexes) {
        String indexStr = i + "" + j;

        return validIndexes.contains(indexStr);
    }
}
