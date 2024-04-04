package ru.tinkoff.hse.dto.responses;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class CustomerCreationResponse {

    private Integer customerId;
}
