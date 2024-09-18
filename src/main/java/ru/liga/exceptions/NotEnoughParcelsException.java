package ru.liga.exceptions;

public class NotEnoughParcelsException extends RuntimeException {
    public NotEnoughParcelsException(String message) {
        super(message);
    }
}
