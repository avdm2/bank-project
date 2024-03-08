package ru.tinkoff.hse.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Setter
@Getter
@Accessors(chain = true)
public class ConverterResponse {

    private String currency;
    private BigDecimal amount;
}