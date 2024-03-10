package ru.tinkoff.hse.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CustomerCreationRequest {

    private String firstName;
    private String lastName;
    private LocalDate birthDay;
}
