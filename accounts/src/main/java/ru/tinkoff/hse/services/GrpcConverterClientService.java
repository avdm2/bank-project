package ru.tinkoff.hse.services;

import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;
import ru.tinkoff.hse.dto.ConverterResponse;
import ru.tinkoff.hse.lib.Converter;
import ru.tinkoff.hse.lib.CurrencyConverterGrpc;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

@Service
public class GrpcConverterClientService {

    private final CurrencyConverterGrpc.CurrencyConverterBlockingStub converterStub;

    public GrpcConverterClientService(CurrencyConverterGrpc.CurrencyConverterBlockingStub converterStub) {
        this.converterStub = converterStub;
    }

    public ConverterResponse convert(String from, String to, BigDecimal amount) {
        Converter.ConvertRequest request = Converter.ConvertRequest.newBuilder()
                .setFromCurrency(from)
                .setToCurrency(to)
                .setAmount(serializeToDecimalValue(amount))
                .build();

        Converter.ConvertResponse response = converterStub.convert(request);
        BigDecimal convertedAmount = deserializeFromDecimalValue(response.getConvertedAmount());
        return new ConverterResponse().setAmount(convertedAmount).setCurrency(to);
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
