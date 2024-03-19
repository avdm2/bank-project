package ru.tinkoff.hse.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Getter
@Setter
@Accessors(chain = true)
public class AccountMessage {

    private Integer accountNumber;
    private String currency;
    private BigDecimal balance;
}
