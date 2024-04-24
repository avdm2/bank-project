package ru.tinkoff.hse.utils;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.tinkoff.hse.services.FeeService;

@Service
public class KafkaListenerService {

    private final FeeService feeService;

    public KafkaListenerService(FeeService feeService) {
        this.feeService = feeService;
    }

    @KafkaListener(topics = "${kafka.topic.fee}")
    public void listen(String message) {
        if (message.contains("UPDATE_FEE")) {
            feeService.init();
        }
    }
}
