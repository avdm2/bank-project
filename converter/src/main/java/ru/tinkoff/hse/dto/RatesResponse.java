package ru.tinkoff.hse.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.tinkoff.hse.dto.components.Currency;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
public class RatesResponse {

    private Currency base;
    private Map<String, BigDecimal> rates;
}
