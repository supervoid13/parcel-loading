package ru.liga.loading.serializers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.liga.loading.exceptions.ParcelValidationException;
import ru.liga.loading.models.Parcel;
import ru.liga.loading.validators.ParcelValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ParcelSerializer {

    private final ParcelValidator parcelValidator;

    /**
     * Десериализация посылок из строки.
     * @param parcelsStr строка с посылками.
     * @return список посылок.
     * @throws ParcelValidationException если снимки посылок в строке невалидны.
     */
    public List<Parcel> deserializeList(String parcelsStr) {

        String[] parcelsStrArray = parcelsStr.split("\\n{2,}");

        List<Parcel> parcels = new ArrayList<>();

        log.info("Parcel validation has started");
        for (String parcelStr : parcelsStrArray) {
            Parcel parcel = deserialize(parcelStr);
            parcel.setSymbol(parcelStr.charAt(0));
            parcelValidator.validate(parcel);
            parcels.add(parcel);
        }
        log.info("All parcels are valid");

        return parcels;
    }

    private Parcel deserialize(String parcelStr) {
        int height = (int) parcelStr.chars().filter(ch -> ch == '\n').count() + 1;
        char[][] box = new char[height][];
        int currentLayer = 1;

        List<Character> charsOnLayer = new ArrayList<>();
        for (int i = parcelStr.length() - 1; i >= 0; i--) {
            if (parcelStr.charAt(i) != '\n') {
                charsOnLayer.add(parcelStr.charAt(i));
                if (i != 0)
                    continue;
            }
            box[height - currentLayer] = charsOnLayer.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining())
                    .toCharArray();

            charsOnLayer = new ArrayList<>();
            currentLayer++;
        }
        return new Parcel(box);
    }
}
