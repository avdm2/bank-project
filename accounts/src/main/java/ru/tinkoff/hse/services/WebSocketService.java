package ru.tinkoff.hse.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.tinkoff.hse.dto.AccountMessage;

@Service
public class WebSocketService {

    private final SimpMessagingTemplate template;

    @Autowired
    public WebSocketService(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void notifyAccountChange(AccountMessage accountMessage) {
        template.convertAndSend("/topic/accounts", accountMessage);
    }
}
