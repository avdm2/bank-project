package ru.tinkoff.hse.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TopUpRequest {

    private BigDecimal amount;
}
