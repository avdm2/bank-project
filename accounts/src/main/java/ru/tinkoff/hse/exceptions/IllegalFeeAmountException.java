package ru.tinkoff.hse.exceptions;

public class IllegalFeeAmountException extends RuntimeException {

    public IllegalFeeAmountException(String message) {
        super(message);
    }
}
