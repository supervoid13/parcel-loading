package ru.liga.loading.serializers;

import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;
import ru.liga.loading.exceptions.TruckValidationException;
import ru.liga.loading.models.Truck;
import ru.liga.loading.validators.TruckJsonValidator;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TruckSerializer {

    private final TruckJsonValidator validator = new TruckJsonValidator();
    private final Gson gson = new Gson();

    /**
     * Десериализация грузовиков из строки
     * @param json строка с грузовиками
     * @return список грузовиков
     * @throws TruckValidationException если снимки кузовов грузовиков в строке невалидны
     */
    public List<Truck> deserialize(String json) {
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
}
