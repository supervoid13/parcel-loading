package ru.liga.loading.readers;

import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;
import ru.liga.loading.models.Truck;
import ru.liga.loading.validators.TruckJsonValidator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TruckJsonReader {

    private final TruckJsonValidator validator = new TruckJsonValidator();

    private final Gson gson = new Gson();

    public List<Truck> readParcelsFromJson(String filePath) throws IOException {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.debug("Method '%s' has started".formatted(methodName));

        String json = stringFromJson(filePath);

        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        JsonArray trucksJsonArray = jsonObject.getAsJsonArray("trucks");

        List<Truck> trucks = new ArrayList<>();

        log.info("Truck validation has started");
        for (JsonElement truckJson : trucksJsonArray) {
            JsonArray jsonArray = truckJson.getAsJsonObject().getAsJsonArray("body");

            char[][] body = gson.fromJson(jsonArray, char[][].class);
            replaceSpaceWithNullChar(body);
            validator.validate(body);

            trucks.add(new Truck(body));
        }
        log.info("Trucks are valid");
        log.debug("Method '%s' has finished".formatted(methodName));
        return trucks;
    }

    private void replaceSpaceWithNullChar(char[][] body) {
        for (char[] layer : body) {
            for (int i = 0; i < layer.length; i++) {
                if (layer[i] == ' ')
                    layer[i] = Truck.EMPTY_SPACE_DESIGNATION;
            }
        }
    }

    private String stringFromJson(String filePath) throws IOException {
        return Files.readString(Path.of(filePath));
    }
}
