package ru.tinkoff.hse.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.hse.dto.components.Currency;
import ru.tinkoff.hse.services.ConverterService;

import java.math.BigDecimal;

@RestController
public class ConverterController {

    private final ConverterService converterService;

    public ConverterController(ConverterService converterService) {
        this.converterService = converterService;
    }

    @GetMapping("/convert")
    public ResponseEntity<?> convert(@RequestParam Currency from, @RequestParam Currency to, @RequestParam BigDecimal amount) {
            return ResponseEntity.ok().body(converterService.convert(from, to, amount));
    }
}
