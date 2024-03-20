package ru.tinkoff.hse.services;

import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.tinkoff.hse.lib.Converter;
import ru.tinkoff.hse.lib.CurrencyConverterGrpc;

import java.lang.module.FindException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Map;

@GrpcService
@Slf4j
public class GrpcConverterServerService extends CurrencyConverterGrpc.CurrencyConverterImplBase {

    private final RatesRequestService ratesRequestService;

    public GrpcConverterServerService(RatesRequestService ratesRequestService) {
        this.ratesRequestService = ratesRequestService;
    }

    @Override
    public void convert(Converter.ConvertRequest request, StreamObserver<Converter.ConvertResponse> responseObserver) {
        log.info("in GrpcConverterServerService::convert");
        Map<String, BigDecimal> rates = ratesRequestService.getRatesFromRequest().getRates();
        BigDecimal amount = deserializeFromDecimalValue(request.getAmount());
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
        Converter.DecimalValue convertedAmount = serializeToDecimalValue(resultAmount);

        Converter.ConvertResponse response = Converter.ConvertResponse.newBuilder()
                .setCurrency(request.getToCurrency())
                .setConvertedAmount(convertedAmount)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private BigDecimal deserializeFromDecimalValue(Converter.DecimalValue value) {
        return new BigDecimal(
                new BigInteger(value.getValue().toByteArray()),
                value.getScale(),
                new MathContext(value.getPrecision())
        );
    }

    private Converter.DecimalValue serializeToDecimalValue(BigDecimal value) {
        return Converter.DecimalValue.newBuilder()
                .setScale(value.scale())
                .setPrecision(value.precision())
                .setValue(ByteString.copyFrom(value.unscaledValue().toByteArray()))
                .build();
    }
}
