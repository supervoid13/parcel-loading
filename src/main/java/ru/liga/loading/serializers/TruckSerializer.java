package ru.liga.loading.serializers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.liga.loading.exceptions.TruckValidationException;
import ru.liga.loading.models.Truck;
import ru.liga.loading.validators.TruckValidator;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TruckSerializer {

    private final TruckValidator validator;
    private final Gson gson;

    /**
     * Десериализация грузовиков из строки
     * @param json строка с грузовиками
     * @return список грузовиков
     * @throws TruckValidationException если снимки кузовов грузовиков в строке невалидны
     */
    public List<Truck> deserialize(String json) {
        JsonElement jsonElement = JsonParser.parseString(json);

        Truck[] trucks = gson.fromJson(jsonElement, Truck[].class);
        List<Truck> truckList = Arrays.asList(trucks);

        log.info("Truck validation has started");
        validator.validateTruckList(truckList);
        log.info("Trucks are valid");

        return truckList;
    }
}
