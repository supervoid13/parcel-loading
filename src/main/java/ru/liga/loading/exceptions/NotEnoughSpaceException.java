package ru.liga.loading.exceptions;

public class NotEnoughSpaceException extends RuntimeException {
    public NotEnoughSpaceException(String message) {
        super(message);
    }
}
