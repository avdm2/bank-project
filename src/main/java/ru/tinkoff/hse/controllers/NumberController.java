package ru.tinkoff.hse.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class NumberController {

    private final int randomNumber = new Random().nextInt();

    @GetMapping("/num")
    public ResponseEntity<Integer> getNumber() {
        System.out.println("request made; num = " + randomNumber);
        return ResponseEntity.ok().body(randomNumber);
    }
}
