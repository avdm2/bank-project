package ru.tinkoff.hse.exceptions;

public class RatesRequestException extends RuntimeException {

    public RatesRequestException(String message) {
        super(message);
    }
}
