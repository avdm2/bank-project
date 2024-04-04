package ru.tinkoff.hse.exceptions;

public class OutboxSendingException extends RuntimeException {

    public OutboxSendingException(String message) {
        super(message);
    }
}
