package ru.tinkoff.hse.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.hse.dto.requests.AccountCreationRequest;
import ru.tinkoff.hse.dto.responses.AccountCreationResponse;
import ru.tinkoff.hse.dto.responses.GetAccountResponse;
import ru.tinkoff.hse.dto.requests.TopUpRequest;
import ru.tinkoff.hse.dto.requests.TransferRequest;
import ru.tinkoff.hse.dto.responses.TransactionResponse;
import ru.tinkoff.hse.services.AccountService;
import ru.tinkoff.hse.services.TransactionService;

import java.util.List;

@RestController
public class AccountController {

    private final AccountService accountService;
    private final TransactionService transactionService;

    public AccountController(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
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
    public ResponseEntity<TransactionResponse> topUp(@PathVariable("accountNumber") Integer accountNumber,
                                                     @RequestBody TopUpRequest request, @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey) {
        return ResponseEntity.ok(accountService.topUp(accountNumber, request, idempotencyKey));
    }

    @PostMapping("/transfers")
    public ResponseEntity<TransactionResponse> transfer(@RequestBody TransferRequest request,
                                                        @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey) {
        return ResponseEntity.ok(accountService.transfer(request, idempotencyKey));
    }

    @GetMapping("/accounts/{accountNumber}/transactions")
    public ResponseEntity<List<TransactionResponse>> getTransactions(@PathVariable("accountNumber") Integer accountNumber) {
        return ResponseEntity.ok().body(transactionService.getAllTransactions(accountNumber));
    }
}
