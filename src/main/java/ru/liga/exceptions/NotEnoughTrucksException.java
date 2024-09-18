package ru.liga.exceptions;

public class NotEnoughTrucksException extends RuntimeException {
    public NotEnoughTrucksException(String message) {
        super(message);
    }
}
