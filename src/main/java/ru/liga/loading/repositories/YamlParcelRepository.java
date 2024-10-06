package ru.liga.loading.repositories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.inspector.TagInspector;
import ru.liga.loading.exceptions.ParcelAlreadyExistException;
import ru.liga.loading.models.Parcel;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class YamlParcelRepository implements ParcelRepository {

    private static final String DATABASE = "db/parcels.yaml";
    private final Map<String, Parcel> parcelMap = initParcels();


    @Override
    public List<Parcel> findAll() {
        return new ArrayList<>(parcelMap.values());
    }

    @Override
    public Optional<Parcel> findBySymbol(char symbol) {
        return findAll().stream()
                .filter(parcel -> parcel.getSymbol() == symbol)
                .findFirst();
    }

    @Override
    public Optional<Parcel> findByName(String name) {
        return Optional.ofNullable(parcelMap.get(name));
    }

    @Override
    public void save(Parcel parcel) {
        String name = parcel.getName();

        if (parcelMap.containsKey(name)) {
            throw new ParcelAlreadyExistException(
                    "Parcel with name '%s' already exist".formatted(name)
            );
        }
        parcelMap.put(name, parcel);
    }

    @Override
    public void update(String name, Parcel parcel) {
        if (!parcelMap.containsKey(name)) {
            throw new NoSuchElementException("No such parcel " + name);
        }

        parcelMap.remove(name);
        parcelMap.put(parcel.getName(), parcel);
    }

    @Override
    public void deleteByName(String name) {
        if (!parcelMap.containsKey(name)) {
            throw new NoSuchElementException("No such parcel " + name);
        }
        parcelMap.remove(name);
    }

    private Map<String, Parcel> initParcels() {
        List<Parcel> parcels = new ArrayList<>();

        LoaderOptions loaderOptions = new LoaderOptions();
        TagInspector tagInspector = tag -> tag.getClassName().equals(Parcel.class.getName());
        loaderOptions.setTagInspector(tagInspector);

        Yaml yaml = new Yaml(new Constructor(Parcel.class, loaderOptions));
        try (FileInputStream inputStream = new FileInputStream(DATABASE)) {
            for (Object o : yaml.loadAll(inputStream)) {
                if (o instanceof Parcel parcel) {
                    parcels.add(parcel);
                }
            }
        } catch (IOException e) {
            log.error("Problem with reading the file");
        }
        return parcels.stream()
                .collect(Collectors.toConcurrentMap(Parcel::getName, parcel -> parcel));
    }
}
