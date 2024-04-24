package ru.tinkoff.hse.services;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.tinkoff.hse.dto.KafkaMessage;
import ru.tinkoff.hse.dto.requests.UpdateFeeRequest;
import ru.tinkoff.hse.entities.Fee;
import ru.tinkoff.hse.exceptions.IllegalFeeAmountException;
import ru.tinkoff.hse.repositories.FeeRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class FeeService {

    private final FeeRepository feeRepository;
    private final KafkaTemplate<String, KafkaMessage> kafkaTemplate;

    private volatile double feeAmount = 0.0;

    @Value("${kafka.topic.fee}")
    private String kafkaTopic;

    public FeeService(FeeRepository feeRepository, KafkaTemplate<String, KafkaMessage> kafkaTemplate) {
        this.feeRepository = feeRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostConstruct
    public void init() {
        Optional<Fee> fee = feeRepository.findFirstByOrderByCreatedAtDesc();
        fee.ifPresent(value -> feeAmount = value.getFee());
    }

    public double getFee() {
        return feeAmount;
    }

    public void updateFee(UpdateFeeRequest request) {
        if (request.getFee() < 0 || request.getFee() > 1) {
            throw new IllegalFeeAmountException("check fee amount");
        }

        feeRepository.save(new Fee().setFee(request.getFee())
                .setCreatedAt(LocalDateTime.now()));

        kafkaTemplate.send(kafkaTopic, new KafkaMessage().setAction("UPDATE_FEE"));
    }
}
