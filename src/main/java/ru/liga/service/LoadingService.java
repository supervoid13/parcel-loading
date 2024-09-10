package ru.liga.service;

import ru.liga.model.Parcel;
import ru.liga.model.Truck;

import java.util.List;

public interface LoadingService {
    List<Truck> loadTrucksWithParcels(List<Parcel> parcels);
}
