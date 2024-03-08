package ru.tinkoff.hse.dto;

import lombok.Getter;

@Getter
public class CustomerCreationRequest {

    private String firstname;
    private String lastname;
    private String birthday;
}
