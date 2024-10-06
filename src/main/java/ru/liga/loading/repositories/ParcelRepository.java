package ru.liga.loading.repositories;

import ru.liga.loading.exceptions.ParcelAlreadyExistException;
import ru.liga.loading.models.Parcel;

import java.util.List;
import java.util.Optional;

public interface ParcelRepository {

    /**
     * Метод для получения существующих посылок.
     * @return список посылок.
     */
    List<Parcel> findAll();

    /**
     * Метод для получения посылки с указанным символом для отображения.
     * @param symbol символ.
     * @return {@code Optional} посылки.
     */
    Optional<Parcel> findBySymbol(char symbol);

    /**
     * Метод получения посылки по имени.
     * @param name имя посылки.
     * @return {@code Optional} посылки.
     */
    Optional<Parcel> findByName(String name);

    /**
     * Метод обновления посылки.
     * @param name имя посылки.
     * @param parcel обновлённая посылка.
     */
    void update(String name, Parcel parcel);

    /**
     * Сохранение посылки в файл.
     * @param parcel посылка.
     * @throws ParcelAlreadyExistException если посылка с таким именем уже существует.
     */
    void save(Parcel parcel);

    /**
     * Метод удаления посылки по имени.
     * @param name имя посылки.
     */
    void deleteByName(String name);
}
