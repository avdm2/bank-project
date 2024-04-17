package ru.tinkoff.hse.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.tinkoff.hse.dto.OutboxMessage;
import ru.tinkoff.hse.entities.OutboxEvent;
import ru.tinkoff.hse.exceptions.OutboxSendingException;
import ru.tinkoff.hse.repositories.OutboxRepository;

import java.util.List;

@Service
public class OutboxService {

    @Value("${notifications.url}")
    private String notificationServiceUrl;

    private final OutboxRepository outboxRepository;

    public OutboxService(OutboxRepository outboxRepository) {
        this.outboxRepository = outboxRepository;
    }

    @Scheduled(fixedDelay = 10_000)
    public void handleMessageSending() {
        List<OutboxEvent> events = outboxRepository.findAllAndLock();
        events.forEach(event -> {
            OutboxMessage outboxMessage = new OutboxMessage().setCustomerId(event.getCustomerId()).setMessage(event.getMessage());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<?> response = new RestTemplate()
                    .exchange(notificationServiceUrl + "/notification",
                            HttpMethod.POST,
                            new HttpEntity<>(outboxMessage, headers),
                            Void.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new OutboxSendingException("message sending error");
            }
            outboxRepository.delete(event);
        });
    }
}
