package ru.tinkoff.hse.controllers;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.tinkoff.hse.dto.AccountMessage;
import ru.tinkoff.hse.entities.Account;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public WebSocketController(SimpMessagingTemplate simpMessagingTemplate) {
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
