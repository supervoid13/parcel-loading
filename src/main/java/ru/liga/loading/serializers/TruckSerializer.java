package ru.liga.loading.serializers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.liga.loading.exceptions.TruckValidationException;
import ru.liga.loading.models.Truck;
import ru.liga.loading.validators.TruckJsonValidator;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TruckSerializer {

    private final TruckJsonValidator validator;
    private final Gson gson;

    /**
     * Десериализация грузовиков из строки
     * @param json строка с грузовиками
     * @return список грузовиков
     * @throws TruckValidationException если снимки кузовов грузовиков в строке невалидны
     * @throws FileNotFoundException если файл с существующими посылками не найден.
     */
    public List<Truck> deserialize(String json) throws FileNotFoundException {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        JsonArray trucksJsonArray = jsonObject.getAsJsonArray("trucks");

        List<Truck> trucks = new ArrayList<>();

        log.info("Truck validation has started");
        for (JsonElement truckJson : trucksJsonArray) {
            JsonArray jsonArray = truckJson.getAsJsonObject().getAsJsonArray("body");

            char[][] body = gson.fromJson(jsonArray, char[][].class);
            validator.validate(body);

            trucks.add(new Truck(body));
        }
        log.info("Trucks are valid");

        return trucks;
    }
}
