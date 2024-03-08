package ru.tinkoff.hse.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;
import ru.tinkoff.hse.entities.ConverterResponse;
import ru.tinkoff.hse.entities.Currency;

import java.lang.module.FindException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Service
public class ConverterService {

    private final RatesRequestService ratesRequestService;

    public ConverterService(RatesRequestService ratesRequestService) {
        this.ratesRequestService = ratesRequestService;
    }

    public ConverterResponse convert(Currency from, Currency to, BigDecimal amount) throws JsonProcessingException {
        Map<String, BigDecimal> rates = ratesRequestService.getRatesFromRequest().getRates();

        if (amount.intValue() <= 0) {
            throw new IllegalArgumentException("Отрицательная сумма");
        }

        if (!rates.containsKey(from.getValue())) {
            throw new FindException("Валюта " + from.getValue() + " недоступна");
        }
        if (!rates.containsKey(to.getValue())) {
            throw new FindException("Валюта " + to.getValue() + " недоступна");
        }

        BigDecimal resultAmount = amount.multiply(rates.get(from.getValue())).divide(rates.get(to.getValue()), 2, RoundingMode.HALF_EVEN);

        return new ConverterResponse().setAmount(resultAmount).setCurrency(to.getValue());
    }
}
