package ru.tinkoff.hse.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.grpc.StatusRuntimeException;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import ru.tinkoff.hse.lib.ConvertRequest;
import ru.tinkoff.hse.lib.ConvertResponse;
import ru.tinkoff.hse.lib.CurrencyConverterGrpc;

import java.math.BigDecimal;

@Service
@DependsOn("grpc_configuration")
public class GrpcConverterClientService {

    private final CurrencyConverterGrpc.CurrencyConverterBlockingStub converterStub;

    public GrpcConverterClientService(CurrencyConverterGrpc.CurrencyConverterBlockingStub converterStub) {
        this.converterStub = converterStub;
    }

    @CircuitBreaker(name = "CircuitBreakerService")
    public ConvertResponse convert(String from, String to, BigDecimal amount) {
        ConvertRequest request = ConvertRequest.newBuilder()
                .setFromCurrency(from)
                .setToCurrency(to)
                .setAmount(amount.toString())
                .build();

        try {
            return converterStub.convert(request);
        } catch (StatusRuntimeException e) {
            return null;
        }
    }
}