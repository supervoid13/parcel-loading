package ru.liga.loading.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.inspector.TagInspector;
import ru.liga.loading.exceptions.ParcelAlreadyExistException;
import ru.liga.loading.models.Parcel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class YamlParcelRepository implements ParcelRepository {

    private static final String DATABASE = "db/parcels.yaml";


    @Override
    public List<Parcel> findAll() throws FileNotFoundException {
        List<Parcel> parcels = new ArrayList<>();

        LoaderOptions loaderOptions = new LoaderOptions();
        TagInspector tagInspector = tag -> tag.getClassName().equals(Parcel.class.getName());
        loaderOptions.setTagInspector(tagInspector);

        Yaml yaml = new Yaml(new Constructor(Parcel.class, loaderOptions));
        FileInputStream inputStream = new FileInputStream(DATABASE);

        for (Object o : yaml.loadAll(inputStream)) {
            if (o instanceof Parcel parcel) {
                parcels.add(parcel);
            }
        }
        return parcels;
    }

    @Override
    public Optional<Parcel> findBySymbol(char symbol) throws FileNotFoundException {
        return findAll().stream()
                .filter(parcel -> parcel.getSymbol() == symbol)
                .findFirst();
    }

    @Override
    public Optional<Parcel> findByName(String name) throws FileNotFoundException {
        List<Parcel> parcels = findAll();

        return parcels.stream()
                .filter(parcel -> name.equals(parcel.getName()))
                .findFirst();
    }

    @Override
    public void save(Parcel parcel) throws IOException {
        List<Parcel> parcels = findAll();

        boolean isPresent = parcels.stream()
                .anyMatch(p -> parcel.getName().equals(p.getName()));

        if (isPresent) throw new ParcelAlreadyExistException("Parcel with this name already exist");

        parcels.add(parcel);
        pushUpdates(parcels);
    }

    @Override
    public void update(String name, Parcel parcel) throws IOException {
        List<Parcel> parcels = findAll();

        Parcel toUpdate = parcels.stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No such parcel"));

        toUpdate.setName(parcel.getName());
        toUpdate.setSymbol(parcel.getSymbol());
        toUpdate.setBox(parcel.getBox());

        pushUpdates(parcels);
    }

    @Override
    public void deleteByName(String name) throws IOException {
        List<Parcel> parcels = findAll();
        Parcel parcel = parcels.stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No such parcel"));

        parcels.remove(parcel);
        pushUpdates(parcels);
    }

    private void pushUpdates(List<Parcel> parcels) throws IOException {
        Yaml yaml = new Yaml();
        FileWriter writer = new FileWriter(DATABASE);
        yaml.dumpAll(parcels.iterator(), writer);
    }
}
