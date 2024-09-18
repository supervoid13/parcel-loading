package ru.liga.utils;

import com.google.gson.*;
import ru.liga.model.Truck;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TruckJsonReader {

    private final Gson gson = new Gson();
    private final static int CHAR_TO_DIGIT_DIFF = 48;

    public Map<Character, Integer> findAndCountParcelsFromJson(String filePath) throws IOException {
        String json = stringFromJson(filePath);
        System.out.println(json);

        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        JsonArray jsonArray = jsonObject.getAsJsonArray("body");

        char[][] body = gson.fromJson(jsonArray, char[][].class);
        replaceSpaceWithCharNull(body);

        return countParcels(body);
    }

    private Map<Character, Integer> countParcels(char[][] body) {
        Map<Character, Integer> result = new HashMap<>();

        for (char[] layer: body) {
            for (char ch: layer) {
                if (ch != Truck.EMPTY_SPACE_DESIGNATION)
                    result.put(ch, result.getOrDefault(ch, 0) + 1);
            }
        }

        return result.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue() / squareFromParcelChar(entry.getKey())
                ));

    }

    private void replaceSpaceWithCharNull(char[][] body) {
        for (char[] layer: body) {
            for (int i = 0; i < layer.length; i++) {
                if (layer[i] == ' ')
                    layer[i] = Truck.EMPTY_SPACE_DESIGNATION;
            }
        }
    }

    private int squareFromParcelChar(char parcelChar) {
        return parcelChar - CHAR_TO_DIGIT_DIFF;
    }

    private String stringFromJson(String filePath) throws IOException {
        return Files.readString(Path.of(filePath));
    }

    public static void main(String[] args) throws IOException {
        System.out.println(new TruckJsonReader().findAndCountParcelsFromJson("truck.json"));
    }
}
