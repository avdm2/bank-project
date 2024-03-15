package ru.tinkoff.hse.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.hse.dto.AccountCreationRequest;
import ru.tinkoff.hse.dto.AccountCreationResponse;
import ru.tinkoff.hse.dto.GetAccountResponse;
import ru.tinkoff.hse.dto.TopUpRequest;
import ru.tinkoff.hse.dto.TransferRequest;
import ru.tinkoff.hse.services.AccountService;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountCreationResponse> create(@RequestBody AccountCreationRequest request) {
        return ResponseEntity.ok().body(accountService.createAccount(request));
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<GetAccountResponse> getAccount(@PathVariable("accountNumber") Integer accountNumber) {
        return ResponseEntity.ok().body(accountService.getAccount(accountNumber));
    }

    @PostMapping("/{accountNumber}/top-up")
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
