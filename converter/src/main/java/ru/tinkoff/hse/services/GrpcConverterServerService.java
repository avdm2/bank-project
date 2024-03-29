package ru.tinkoff.hse.services;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.tinkoff.hse.lib.ConvertRequest;
import ru.tinkoff.hse.lib.ConvertResponse;
import ru.tinkoff.hse.lib.CurrencyConverterGrpc;

import java.lang.module.FindException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@GrpcService
public class GrpcConverterServerService extends CurrencyConverterGrpc.CurrencyConverterImplBase {

    private final RatesRequestService ratesRequestService;

    public GrpcConverterServerService(RatesRequestService ratesRequestService) {
        this.ratesRequestService = ratesRequestService;
    }

    @Override
    public void convert(ConvertRequest request, StreamObserver<ConvertResponse> responseObserver) {
        Map<String, BigDecimal> rates = ratesRequestService.getRatesFromRequest().getRates();
        BigDecimal amount = new BigDecimal(request.getAmount());
        String from = request.getFromCurrency();
        String to = request.getToCurrency();

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
        String convertedAmount = resultAmount.toPlainString();

        ConvertResponse response = ConvertResponse.newBuilder()
                .setCurrency(request.getToCurrency())
                .setConvertedAmount(convertedAmount)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}