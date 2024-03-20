package ru.tinkoff.hse.configurations;

import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.client.inject.GrpcClientBean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.hse.lib.CurrencyConverterGrpc;

@Configuration(proxyBeanMethods = false)
@GrpcClientBean(
        clazz = CurrencyConverterGrpc.CurrencyConverterFutureStub.class,
        beanName = "grpcStub",
        client = @GrpcClient(
                value = "grpcConverterClientService"
        )
)
public class GrpcConfiguration {
}
