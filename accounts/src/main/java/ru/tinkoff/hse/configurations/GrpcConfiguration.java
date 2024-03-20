package ru.tinkoff.hse.configurations;

import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.client.inject.GrpcClientBean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.hse.lib.CurrencyConverterGrpc.CurrencyConverterBlockingStub;

@Configuration(proxyBeanMethods = false)
@GrpcClientBean(
        clazz = CurrencyConverterBlockingStub.class,
        beanName = "converterStub",
        client = @GrpcClient(
                value = "grpcConverterClientService"
        )
)
public class GrpcConfiguration {
}
