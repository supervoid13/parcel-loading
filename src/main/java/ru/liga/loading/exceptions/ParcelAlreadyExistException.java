package ru.liga.loading.exceptions;

public class ParcelAlreadyExistException extends RuntimeException {
    public ParcelAlreadyExistException(String message) {
        super(message);
    }
}
