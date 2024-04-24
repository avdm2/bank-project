package ru.tinkoff.hse.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class KafkaMessage {

    private String action;
}
