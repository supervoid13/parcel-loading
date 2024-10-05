package ru.liga.loading.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.liga.loading.exceptions.ParcelAlreadyExistException;
import ru.liga.loading.models.Parcel;
import ru.liga.loading.readers.ParcelReader;
import ru.liga.loading.repositories.ParcelRepository;
import ru.liga.loading.validators.ParcelValidator;
import ru.liga.loading.view.ParcelView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class ParcelService {

    private final ParcelReader parcelReader;
    private final ParcelRepository parcelRepository;
    private final ParcelView parcelView;
    private final ParcelValidator parcelValidator;

    /**
     * Чтение посылок из файла.
     * @param filePath путь к файлу.
     * @return список посылок.
     */
    public List<Parcel> readParcelsFromFile(String filePath) {
        return parcelReader.readParcelsFromFile(filePath);
    }

    /**
     * Метод получения посылок по их именам.
     * @param names имена посылок.
     * @return список посылок.
     */
    public List<Parcel> getParcelsByNames(String[] names) {
        List<Parcel> parcels = new ArrayList<>();
        for (String name : names) {
            parcels.add(getByName(name));
        }
        return parcels;
    }

    /**
     * Метод получения всех посылок в виде удобно-читаемой строки.
     * @return строку, отображающую список посылок.
     */
    public String getPrettyOutputForAllParcels() {
        return parcelView.convertParcelsToPrettyOutput(getAllParcels());
    }

    /**
     * Метод получения посылки в удобно-читаемом виде.
     * @param name имя посылки.
     * @return строку, отображающую посылку.
     */
    public String getPrettyOutputForParcelByName(String name) {
        return getByName(name).convertToPrettyOutput();
    }

    /**
     * Метод сохранения новой посылки.
     * @param name имя посылки.
     * @param symbol символ формы.
     * @throws ParcelAlreadyExistException если посылка с таким именем уже существует.
     */
    public void save(String name, char symbol) {
        Parcel parcel = parcelReader.readParcel(name, symbol);
        parcelValidator.validateBox(parcel);
        parcelRepository.save(parcel);
    }

    /**
     * Метод изменения посылки по её имени.
     * @param newName имя посылки.
     * @throws NoSuchElementException если посылка с таким именем не найдена.
     */
    public void update(String name, String newName, char newSymbol) {
        Parcel parcel = parcelReader.readParcel(newName, newSymbol);

        if (!newName.isBlank())
            parcel.setName(newName);

        if (newSymbol != ' ') {
            parcel.setSymbol(newSymbol);
        }
        parcelValidator.validateBox(parcel);
        parcelRepository.update(name, parcel);
    }

    public void delete(String name) throws IOException {
        parcelRepository.deleteByName(name);
    }

    private List<Parcel> getAllParcels() {
        return parcelRepository.findAll();
    }

    private Parcel getByName(String name) {
        return parcelRepository.findByName(name).orElseThrow(() -> new NoSuchElementException("No such parcel " + name));
    }
}
