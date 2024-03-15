package ru.tinkoff.hse.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class TopUpRequest {

    private BigDecimal amount;
}
