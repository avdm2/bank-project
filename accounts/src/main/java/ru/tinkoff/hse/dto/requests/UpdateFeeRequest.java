package ru.tinkoff.hse.dto.requests;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class UpdateFeeRequest {

    private double fee;
}
