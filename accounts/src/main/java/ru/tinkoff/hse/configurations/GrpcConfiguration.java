package ru.tinkoff.hse.configurations;

import io.grpc.Channel;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.client.inject.GrpcClientBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.hse.lib.CurrencyConverterGrpc;

@Configuration(proxyBeanMethods = false)

public class GrpcConfiguration {

        @GrpcClient("converter")
        private Channel channel;

        @Bean
        public CurrencyConverterGrpc.CurrencyConverterBlockingStub currencyConverterBlockingStub() {
                return CurrencyConverterGrpc.newBlockingStub(channel);
        }
}
