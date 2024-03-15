package ru.tinkoff.hse.models;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Getter
@Setter
@Accessors(chain = true)
public class ConverterResponse {

    private String currency;
    private BigDecimal amount;
}
