package ru.liga.loading.repositories;

import ru.liga.loading.exceptions.ParcelAlreadyExistException;
import ru.liga.loading.models.Parcel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ParcelRepository {

    /**
     * Метод для получения существующих посылок.
     * @return список посылок.
     * @throws FileNotFoundException если не найден файл с посылками.
     */
    List<Parcel> findAll() throws FileNotFoundException;

    /**
     * Метод для получения посылки с указанным символом для отображения.
     * @param symbol символ.
     * @return {@code Optional} посылки.
     * @throws FileNotFoundException если не найден файл с посылками.
     */
    Optional<Parcel> findBySymbol(char symbol) throws FileNotFoundException;

    /**
     * Метод получения посылки по имени.
     * @param name имя посылки.
     * @return {@code Optional} посылки.
     * @throws FileNotFoundException если не найден файл с посылками.
     */
    Optional<Parcel> findByName(String name) throws FileNotFoundException;

    /**
     * Метод обновления посылки.
     * @param name имя посылки.
     * @param parcel обновлённая посылка.
     * @throws IOException если произошла ошибка работы с файлом с посылками.
     */
    void update(String name, Parcel parcel) throws IOException;

    /**
     * Сохранение посылки в файл.
     * @param parcel посылка.
     * @throws IOException если произошла ошибка работы с файлом с посылками.
     * @throws ParcelAlreadyExistException если посылка с таким именем уже существует.
     */
    void save(Parcel parcel) throws IOException;

    /**
     * Метод удаления посылки по имени.
     * @param name имя посылки.
     * @throws IOException если произошла ошибка работы с файлом с посылками.
     */
    void deleteByName(String name) throws IOException;
}
