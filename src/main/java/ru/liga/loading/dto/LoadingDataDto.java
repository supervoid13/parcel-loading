package ru.liga.loading.dto;

import lombok.Value;
import ru.liga.loading.enums.LoadingMode;
import ru.liga.loading.models.Parcel;

import javax.validation.constraints.Min;
import java.util.List;

@Value
public class LoadingDataDto {

    LoadingMode mode;
    @Min(1)
    int trucks;
    @Min(1)
    int height;
    @Min(1)
    int width;
    List<Parcel> parcels;
    List<String> parcelNames;
}
