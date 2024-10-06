package ru.liga.loading.view;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.liga.loading.models.Parcel;

import java.util.List;

@Slf4j
@Component
public class ParcelView {


    public String convertParcelsToPrettyOutput(List<Parcel> parcels) {
        log.trace("Converting parcels to pretty output");

        StringBuilder sb = new StringBuilder();

        for (Parcel parcel : parcels) {
            sb.append(parcel.convertToPrettyOutput()).append("\n");
        }
        return sb.toString();
    }
}
