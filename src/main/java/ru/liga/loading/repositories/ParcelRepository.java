package ru.liga.loading.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.liga.loading.models.Parcel;

import java.util.Optional;

@Repository
public interface ParcelRepository extends JpaRepository<Parcel, String> {
    Optional<Parcel> findBySymbol(char symbol);
    Optional<Parcel> findByName(String name);
}
