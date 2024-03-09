package ru.tinkoff.hse.dto;

import lombok.Getter;

@Getter
public class CustomerCreationRequest {

    private String firstName;
    private String lastName;
    private String birthDay;
}
