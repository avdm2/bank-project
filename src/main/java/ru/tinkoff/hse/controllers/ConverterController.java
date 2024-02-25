package ru.tinkoff.hse.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import ru.tinkoff.hse.models.ConverterError;
import ru.tinkoff.hse.services.ConverterService;


import java.lang.module.FindException;
import java.math.BigDecimal;
import java.net.ConnectException;

@RestController
public class ConverterController {

    private final ConverterService converterService;

    public ConverterController(ConverterService converterService) {
        this.converterService = converterService;
    }

    @GetMapping
    public ResponseEntity<?> convert(@RequestParam String from, @RequestParam String to, @RequestParam BigDecimal amount)
            throws JsonProcessingException, ConnectException {
        try {
            return ResponseEntity.ok().body(converterService.convert(from, to, amount));
        } catch (IllegalArgumentException | FindException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ConverterError().setMessage(exception.getMessage()));
        }
    }
}
