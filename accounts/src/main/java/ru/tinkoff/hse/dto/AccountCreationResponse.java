package ru.tinkoff.hse.dto;

import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(chain = true)
public class AccountCreationResponse {

    private Integer accountNumber;
}
