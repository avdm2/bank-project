package ru.tinkoff.hse.configurations;

import io.grpc.Channel;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.hse.lib.CurrencyConverterGrpc;

@Configuration
public class GrpcConfiguration {

        @GrpcClient("grpcConverterClientService")
        private Channel channel;

        @Bean
        public CurrencyConverterGrpc.CurrencyConverterBlockingStub converterStub() {
                return CurrencyConverterGrpc.newBlockingStub(channel);
        }
}
