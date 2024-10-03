package ru.liga.loading.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.liga.loading.exceptions.ParcelAlreadyExistException;
import ru.liga.loading.models.Parcel;
import ru.liga.loading.readers.ParcelReader;
import ru.liga.loading.repositories.ParcelRepository;
import ru.liga.loading.validators.ParcelValidator;
import ru.liga.loading.view.ParcelView;

import java.io.FileNotFoundException;
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
     * @throws IOException если при чтении из файла произошла ошибка ввода-вывода или была прочитана
     * некорректная или неотображаемая последовательность байтов.
     */
    public List<Parcel> readParcelsFromFile(String filePath) throws IOException {
        return parcelReader.readParcelsFromFile(filePath);
    }

    public List<Parcel> getParcelsByNames(String[] names) throws FileNotFoundException {
        List<Parcel> parcels = new ArrayList<>();
        for (String name : names) {
            parcels.add(getByName(name));
        }
        return parcels;
    }

    /**
     * Метод сохранения новой посылки.
     * @param name имя посылки.
     * @param symbol символ формы.
     * @throws ParcelAlreadyExistException если посылка с таким именем уже существует.
     * @throws IOException если произошла ошибка чтения формы посылки.
     * @throws FileNotFoundException если файл с посылки не найден.
     */
    public void save(String name, char symbol) throws IOException {
        Parcel parcel = parcelReader.readParcel(name, symbol);
        parcelValidator.validateBox(parcel);
        parcelRepository.save(parcel);
    }

    /**
     * Метод изменения посылки по её имени.
     * @param newName имя посылки.
     * @throws FileNotFoundException если файл с посылки не найден.
     * @throws IOException если произошла ошибка работы с файлом с посылками.
     * @throws NoSuchElementException если посылка с таким именем не найдена.
     */
    public void update(String name, String newName, char newSymbol) throws IOException {
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

    /**
     * Метод отображения списка существующих посылок.
     * @throws FileNotFoundException если файл с посылки не найден.
     */
    public void displayParcels() throws FileNotFoundException {
        parcelView.displayParcels(getAllParcels());
    }

    /**
     * Метод отображения посылки по имени.
     * @param name имя посылки.
     * @throws FileNotFoundException если файл с посылки не найден.
     * @throws NoSuchElementException если посылка с таким именем не найдена.
     */
    public void displayParcel(String name) throws FileNotFoundException {
        parcelView.displayParcel(getByName(name));
    }

    private List<Parcel> getAllParcels() throws FileNotFoundException {
        return parcelRepository.findAll();
    }

    private Parcel getByName(String name) throws FileNotFoundException {
        return parcelRepository.findByName(name).orElseThrow(() -> new NoSuchElementException("No such parcel"));
    }
}
