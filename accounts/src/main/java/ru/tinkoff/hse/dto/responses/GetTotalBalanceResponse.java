package ru.tinkoff.hse.dto.responses;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Setter
@Getter
@Accessors(chain = true)
public class GetTotalBalanceResponse {

    private BigDecimal balance;
    private String currency;
}
