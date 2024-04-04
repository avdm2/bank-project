package ru.tinkoff.hse.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.hse.dto.requests.AccountCreationRequest;
import ru.tinkoff.hse.dto.responses.AccountCreationResponse;
import ru.tinkoff.hse.dto.responses.GetAccountResponse;
import ru.tinkoff.hse.dto.requests.TopUpRequest;
import ru.tinkoff.hse.dto.requests.TransferRequest;
import ru.tinkoff.hse.services.AccountService;

@RestController
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/accounts")
    public ResponseEntity<AccountCreationResponse> create(@RequestBody AccountCreationRequest request) {
        return ResponseEntity.ok().body(accountService.createAccount(request));
    }

    @GetMapping("/accounts/{accountNumber}")
    public ResponseEntity<GetAccountResponse> getAccount(@PathVariable("accountNumber") Integer accountNumber) {
        return ResponseEntity.ok().body(accountService.getAccount(accountNumber));
    }

    @PostMapping("/accounts/{accountNumber}/top-up")
    public ResponseEntity<?> topUp(@PathVariable("accountNumber") Integer accountNumber, @RequestBody TopUpRequest request) {
        accountService.topUp(accountNumber, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/transfers")
    public ResponseEntity<?> transfer(@RequestBody TransferRequest request) {
        accountService.transfer(request);
        return ResponseEntity.ok().build();
    }
}
