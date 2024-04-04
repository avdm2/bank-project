package ru.tinkoff.hse.models.enums;

import lombok.Getter;

@Getter
public enum OperationType {

    ADD("+"),
    SUBTRACT("-");

    private final String value;

    OperationType(String value) {
        this.value = value;
    }
}
