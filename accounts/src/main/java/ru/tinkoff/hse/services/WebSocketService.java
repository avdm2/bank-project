package ru.tinkoff.hse.services;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.tinkoff.hse.dto.AccountMessage;
import ru.tinkoff.hse.entities.Account;

@Service
public class WebSocketService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public WebSocketService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void sendAccountUpdate(Account account) {
        AccountMessage message = new AccountMessage()
                .setAccountNumber(account.getAccountNumber())
                .setCurrency(account.getCurrency())
                .setBalance(account.getAmount());
        simpMessagingTemplate.convertAndSend("/topic/account", account);
    }
}
