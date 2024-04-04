package ru.tinkoff.hse.dto.requests;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class TopUpRequest {

    private BigDecimal amount;
}
