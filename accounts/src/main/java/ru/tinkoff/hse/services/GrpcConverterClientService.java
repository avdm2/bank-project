package ru.tinkoff.hse.services;

import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tinkoff.hse.dto.ConverterResponse;
import ru.tinkoff.hse.lib.Converter;
import ru.tinkoff.hse.lib.Converter.DecimalValue;
import ru.tinkoff.hse.lib.CurrencyConverterGrpc.CurrencyConverterBlockingStub;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

@Service
@Slf4j
public class GrpcConverterClientService {

    private final CurrencyConverterBlockingStub converterStub;

    public GrpcConverterClientService(CurrencyConverterBlockingStub converterStub) {
        this.converterStub = converterStub;
    }

    public ConverterResponse convert(String from, String to, BigDecimal amount) {
        log.info("in GrpcConverterClientService::convert. from: {}, to: {}, amount: {}", from, to, amount);
        Converter.ConvertRequest request = Converter.ConvertRequest.newBuilder()
                .setFromCurrency(from)
                .setToCurrency(to)
                .setAmount(serializeToDecimalValue(amount))
                .build();

        Converter.ConvertResponse response = converterStub.convert(request);
        BigDecimal convertedAmount = deserializeFromDecimalValue(response.getConvertedAmount());
        return new ConverterResponse().setAmount(convertedAmount).setCurrency(to);
    }

    private BigDecimal deserializeFromDecimalValue(DecimalValue value) {
        return new BigDecimal(
                new BigInteger(value.getValue().toByteArray()),
                value.getScale(),
                new MathContext(value.getPrecision())
        );
    }

    private DecimalValue serializeToDecimalValue(BigDecimal value) {
        return DecimalValue.newBuilder()
                .setScale(value.scale())
                .setPrecision(value.precision())
                .setValue(ByteString.copyFrom(value.unscaledValue().toByteArray()))
                .build();
    }
}
