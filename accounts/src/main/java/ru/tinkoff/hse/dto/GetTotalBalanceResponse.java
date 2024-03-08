package ru.tinkoff.hse.dto;

import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Setter
@Accessors(chain = true)
public class GetTotalBalanceResponse {

    private BigDecimal balance;
    private String currency;
}
