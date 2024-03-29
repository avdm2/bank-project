package ru.tinkoff.hse.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import ru.tinkoff.hse.dto.ConverterResponse;
import ru.tinkoff.hse.lib.ConvertRequest;
import ru.tinkoff.hse.lib.ConvertResponse;
import ru.tinkoff.hse.lib.CurrencyConverterGrpc.CurrencyConverterBlockingStub;

import java.math.BigDecimal;

@Service
public class GrpcConverterClientService {

    private final CurrencyConverterBlockingStub converterStub;

    public GrpcConverterClientService(CurrencyConverterBlockingStub converterStub) {
        this.converterStub = converterStub;
    }

    @CircuitBreaker(name = "grpcConverterCircuitBreaker", fallbackMethod = "fallback")
    public ConverterResponse convert(String from, String to, BigDecimal amount) {
        ConvertRequest request = ConvertRequest.newBuilder()
                .setFromCurrency(from)
                .setToCurrency(to)
                .setAmount(amount.toPlainString())
                .build();

        ConvertResponse response = converterStub.convert(request);
        BigDecimal convertedAmount = new BigDecimal(response.getConvertedAmount());
        return new ConverterResponse().setAmount(convertedAmount).setCurrency(to);
    }

    public ConverterResponse fallback(Throwable t) {
        return new ConverterResponse();
    }
}
