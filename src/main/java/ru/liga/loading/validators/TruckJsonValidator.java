package ru.liga.loading.validators;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.liga.loading.exceptions.TruckValidationException;
import ru.liga.loading.models.Parcel;
import ru.liga.loading.models.Truck;
import ru.liga.loading.repositories.ParcelRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class TruckJsonValidator {

    private final ParcelRepository parcelRepository;

    /**
     * Метод валидации кузовов списка грузовиков.
     * @param trucks список грузовиков.
     */
    public void validateTruckList(List<Truck> trucks) {
        for (Truck truck : trucks) {
            validateTruck(truck);
        }
    }

    private void validateTruck(Truck truck) {
        char[][] body = truck.getBody();
        Set<String> validIndexes = new HashSet<>();
        int height = body.length;

        for (int i = height - 1; i >= 0; i--) {
            for (int j = 0; j < body[i].length; j++) {
                char parcelChar = body[i][j];

                if (isCharAtIndexesAlreadyChecked(i, j, validIndexes) || isCharRepresentNullValue(parcelChar)) {
                    continue;
                }

                boolean isParcelInTruckValid = isParcelFormInTrucksValid(parcelChar, body, i, j, validIndexes);

                if (!isParcelInTruckValid)
                    throw new TruckValidationException("Invalid truck json");
            }
        }
    }

    private boolean isParcelFormInTrucksValid(
            char parcelChar,
            char[][] body,
            int height,
            int width,
            Set<String> validIndexes
    ) {
        Optional<Parcel> bySymbolOpt = parcelRepository.findBySymbol(parcelChar);

        Parcel parcel = bySymbolOpt.orElseThrow(
                () -> new TruckValidationException("Invalid truck json (no parcel with symbol " + parcelChar)
        );

        return doesMatchParcelForm(parcel, body, height, width, validIndexes);
    }

    private boolean doesMatchParcelForm(Parcel parcel, char[][] body, int height, int width, Set<String> validIndexes) {
        char[][] box = parcel.getBox();

        try {
            for (int h = box.length - 1; h >= 0; h--) {
                for (int w = 0; w < box[h].length; w++) {
                    if (box[h][w] != body[height][w + width])
                        return false;

                    String indexStr = (height) + "" + (w + width);
                    validIndexes.add(indexStr);
                }
                height--;
            }
        } catch (IndexOutOfBoundsException e) {
            log.error("Invalid json file with trucks");
            throw new TruckValidationException("Invalid truck json");
        }
        return true;
    }

    private boolean isCharRepresentNullValue(char parcelChar) {
        return parcelChar == Truck.EMPTY_SPACE_DESIGNATION;
    }

    private boolean isCharAtIndexesAlreadyChecked(int i, int j, Set<String> validIndexes) {
        String indexStr = i + "" + j;
        return validIndexes.contains(indexStr);
    }
}
