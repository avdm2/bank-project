package ru.tinkoff.hse.models;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
public class RatesResponse {

    private Currency base;
    private Map<String, BigDecimal> rates;
}
