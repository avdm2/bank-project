package ru.tinkoff.hse.dto.requests;

import lombok.Getter;

@Getter
public class AccountCreationRequest {

    private Integer customerId;
    private String currency;
}
