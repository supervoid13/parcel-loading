package ru.liga.loading.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.liga.loading.exceptions.ParcelAlreadyExistException;
import ru.liga.loading.models.Parcel;
import ru.liga.loading.readers.ParcelReader;
import ru.liga.loading.repositories.ParcelRepository;
import ru.liga.loading.validators.ParcelValidator;
import ru.liga.loading.view.ParcelView;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
    public List<Parcel> getParcelsByNames(List<String> names) {
        List<Parcel> parcels = new ArrayList<>();
        for (String name : names) {
            parcels.add(getParcelByName(name));
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
        return getParcelByName(name).convertToPrettyOutput();
    }

    /**
     * Метод создания новой посылки.
     * @param name имя посылки.
     * @param symbol символ формы.
     * @throws ParcelAlreadyExistException если посылка с таким именем уже существует.
     */
    @Transactional
    public void createParcel(String name, char symbol) {
        Parcel parcel = parcelReader.readParcelFromConsole(name, symbol);
        saveParcel(parcel);
    }

    /**
     * Метод создания новой посылки, имея все данные.
     * @param parcel посылка.
     */
    @Transactional
    public void saveParcel(Parcel parcel) {
        parcelValidator.validateBox(parcel);
        parcelRepository.save(parcel);
    }

    /**
     * Метод изменения посылки по её имени.
     * @param newName имя посылки.
     * @throws NoSuchElementException если посылки с таким именем не существует.
     */
    @Transactional
    public void updateParcel(String name, String newName, char newSymbol) {
        Parcel parcel = parcelReader.readParcelFromConsole(newName, newSymbol);

        if (!newName.isBlank())
            parcel.setName(newName);

        if (newSymbol != ' ') {
            parcel.setSymbol(newSymbol);
        }
        updateParcelHavingBox(name, parcel);
    }

    /**
     * Метод обновления данных посылки по имени.
     * @param name имя посылки.
     * @param parcel посылка, содержащая новые данные.
     * @throws NoSuchElementException если посылки с таким именем не существует.
     */
    @Transactional
    public void updateParcelHavingBox(String name, Parcel parcel) {
        Optional<Parcel> parcelOpt = parcelRepository.findByName(name);
        Parcel toUpdate = parcelOpt.orElseThrow(() -> new NoSuchElementException("No such parcel"));

        toUpdate.setName(parcel.getName());
        toUpdate.setSymbol(parcel.getSymbol());
        toUpdate.setBox(parcel.getBox());

        saveParcel(toUpdate);
    }

    /**
     * Метод удаления посылки по имени.
     * @param name имя посылки.
     * @throws NoSuchElementException если посылки с таким именем не существует.
     */
    @Transactional
    public void deleteParcel(String name) {
        Parcel parcel = parcelRepository.findByName(name).orElseThrow(
                () -> new NoSuchElementException("No such parcel")
        );
        parcelRepository.delete(parcel);
    }

    /**
     * Метод получения всех посылок.
     * @return список посылок.
     */
    public List<Parcel> getAllParcels() {
        return parcelRepository.findAll();
    }

    /**
     * Метод получения посылки по имени.
     * @param name имя посылки.
     * @return посылку с указанным именем.
     * @throws NoSuchElementException если посылки с таким именем не существует.
     */
    public Parcel getParcelByName(String name) {
        return parcelRepository.findByName(name).orElseThrow(
                () -> new NoSuchElementException("No such parcel " + name)
        );
    }
}
