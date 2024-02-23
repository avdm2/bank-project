package ru.tinkoff.hse.models;

import java.math.BigDecimal;
import java.util.Map;

public class RatesResponse {

    private Currency base;
    private Map<String, BigDecimal> rates;
}
