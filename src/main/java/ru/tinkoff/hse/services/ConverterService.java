package ru.tinkoff.hse.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;
import ru.tinkoff.hse.models.ConverterResponse;

import java.lang.module.FindException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.ConnectException;
import java.util.Map;

@Service
public class ConverterService {

    private final RatesRequestService ratesRequestService;

    public ConverterService(RatesRequestService ratesRequestService) {
        this.ratesRequestService = ratesRequestService;
    }

    public ConverterResponse convert(String from, String to, BigDecimal amount) throws JsonProcessingException,
            ConnectException {
        Map<String, BigDecimal> rates = ratesRequestService.getRatesFromRequest().getRates();

        if (amount.intValue() <= 0) {
            throw new IllegalArgumentException("Отрицательная сумма");
        }

        if (!rates.containsKey(from)) {
            throw new FindException("Валюта " + from + " недоступна");
        }
        if (!rates.containsKey(to)) {
            throw new FindException("Валюта " + to + " недоступна");
        }

        BigDecimal resultAmount = amount.multiply(rates.get(from)).divide(rates.get(to), 2, RoundingMode.HALF_EVEN);

        return new ConverterResponse().setAmount(resultAmount).setCurrency(to);
    }
}
