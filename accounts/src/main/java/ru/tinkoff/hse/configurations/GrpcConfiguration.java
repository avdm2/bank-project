package ru.tinkoff.hse.configurations;

import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.client.inject.GrpcClientBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.hse.lib.CurrencyConverterGrpc.CurrencyConverterBlockingStub;
import ru.tinkoff.hse.services.GrpcConverterClientService;

@Configuration(proxyBeanMethods = false)
@GrpcClientBean(
        clazz = CurrencyConverterBlockingStub.class,
        beanName = "converterStub",
        client = @GrpcClient(
                value = "grpcConverterClientService"
        )
)
public class GrpcConfiguration {

    @Bean
    public GrpcConverterClientService grpcConverterClientService(@Autowired CurrencyConverterBlockingStub converterStub) {
        return new GrpcConverterClientService(converterStub);
    }
}
