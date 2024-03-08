package ru.tinkoff.hse.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class TransferRequest {

    private Integer receiverAccount;
    private Integer senderAccount;
    private BigDecimal amountInSenderCurrency;
}
