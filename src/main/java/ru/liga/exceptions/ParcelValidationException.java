package ru.liga.exceptions;

public class ParcelValidationException extends RuntimeException {
    public ParcelValidationException(String message) {
        super(message);
    }
}
