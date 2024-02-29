package ru.tinkoff.hse.models;

public enum Currency {

    RUB("RUB"),
    CNY("CNY"),
    EUR("EUR"),
    USD("USD"),
    GBP("GBP");

    private final String value;

    Currency(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
