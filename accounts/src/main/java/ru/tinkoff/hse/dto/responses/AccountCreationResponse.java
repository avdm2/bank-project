package ru.tinkoff.hse.dto.responses;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class AccountCreationResponse {

    private Integer accountNumber;
}
