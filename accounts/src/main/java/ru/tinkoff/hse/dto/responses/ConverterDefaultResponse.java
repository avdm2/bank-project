package ru.tinkoff.hse.dto.responses;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Setter
@Getter
@Accessors(chain = true)
public class ConverterDefaultResponse {

    private String currency;
    private BigDecimal amount;
}
