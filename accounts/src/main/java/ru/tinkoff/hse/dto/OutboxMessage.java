package ru.tinkoff.hse.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class OutboxMessage {

    private Integer customerId;
    private String message;
}
