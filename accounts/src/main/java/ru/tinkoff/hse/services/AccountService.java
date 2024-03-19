package ru.tinkoff.hse.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.hse.controllers.WebSocketController;
import ru.tinkoff.hse.dto.AccountCreationRequest;
import ru.tinkoff.hse.dto.AccountCreationResponse;
import ru.tinkoff.hse.dto.ConverterResponse;
import ru.tinkoff.hse.dto.GetAccountResponse;
import ru.tinkoff.hse.dto.TopUpRequest;
import ru.tinkoff.hse.dto.TransferRequest;
import ru.tinkoff.hse.entities.Account;
import ru.tinkoff.hse.repositories.AccountRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final GrpcConverterClientService grpcConverterClientService;
    private final WebSocketController webSocketController;

    @Value("${app.converter-url}")
    private String converterUrl;

    public AccountService(AccountRepository accountRepository,
                          GrpcConverterClientService grpcConverterClientService,
                          WebSocketController webSocketController) {
        this.accountRepository = accountRepository;
        this.grpcConverterClientService = grpcConverterClientService;
        this.webSocketController = webSocketController;
    }

    public AccountCreationResponse createAccount(AccountCreationRequest request) {
        if (request.getCurrency() == null || request.getCustomerId() == null) {
            throw new IllegalArgumentException("check required fields");
        }

        if (!List.of("RUB", "USD", "GBP", "EUR", "CYN").contains(request.getCurrency())) {
            throw new IllegalArgumentException("illegal currency");
        }

        if (accountRepository.findByCustomerIdAndCurrency(request.getCustomerId(), request.getCurrency()).isPresent()) {
            throw new IllegalArgumentException("account with such currency already exists");
        }

        Account account = new Account()
                .setCustomerId(request.getCustomerId())
                .setCurrency(request.getCurrency());
        accountRepository.save(account);

        webSocketController.sendAccountUpdate(account);

        return new AccountCreationResponse().setAccountNumber(account.getAccountNumber());
    }

    public GetAccountResponse getAccount(Integer accountNumber) {
        Optional<Account> optionalAccount = accountRepository.findByAccountNumber(accountNumber);
        if (optionalAccount.isEmpty()) {
            throw new IllegalArgumentException("account with such id not found");
        }

        Account account = optionalAccount.get();

        return new GetAccountResponse()
                .setAmount(account.getAmount())
                .setCurrency(account.getCurrency());
    }

    public void topUp(Integer accountNumber, TopUpRequest request) {
        BigDecimal requestAmount = request.getAmount();
        if (requestAmount == null) {
            throw new IllegalArgumentException("check required fields");
        }

        if (requestAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("invalid amount to top up");
        }

        Optional<Account> optionalAccount = accountRepository.findByAccountNumber(accountNumber);
        if (optionalAccount.isEmpty()) {
            throw new IllegalArgumentException("account with such id not found");
        }

        Account account = optionalAccount.get();
        BigDecimal newAmount = account.getAmount().add(requestAmount);
        account.setAmount(newAmount);
        accountRepository.save(account);

        webSocketController.sendAccountUpdate(account);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void transfer(TransferRequest request) {
        if (request.getReceiverAccount() == null || request.getSenderAccount() == null || request.getAmountInSenderCurrency() == null) {
            throw new IllegalArgumentException("check required fields");
        }

        Optional<Account> optionalSenderAccount = accountRepository.findByAccountNumber(request.getSenderAccount());
        Optional<Account> optionalReceiverAccount = accountRepository.findByAccountNumber(request.getReceiverAccount());
        if (optionalSenderAccount.isEmpty()) {
            throw new IllegalArgumentException("sender account not found");
        }
        if (optionalReceiverAccount.isEmpty()) {
            throw new IllegalArgumentException("receiver account not found");
        }

        Account senderAccount = optionalSenderAccount.get();
        Account receiverAccount = optionalReceiverAccount.get();
        BigDecimal amountInSenderCurrency = request.getAmountInSenderCurrency();
        if (senderAccount.getAmount().compareTo(amountInSenderCurrency) < 0) {
            throw new IllegalArgumentException("amount to transfer > available funds");
        }

        if (senderAccount.getCurrency().equals(receiverAccount.getCurrency())) {
            receiverAccount.setAmount(receiverAccount.getAmount().add(amountInSenderCurrency));
            senderAccount.setAmount(senderAccount.getAmount().subtract(amountInSenderCurrency));
        } else {

            ConverterResponse converterResponse = grpcConverterClientService.convert(senderAccount.getCurrency(), receiverAccount.getCurrency(), amountInSenderCurrency);
            if (converterResponse == null) {
                throw new NullPointerException("error with gotten response from converter");
            }
            receiverAccount.setAmount(receiverAccount.getAmount().add(converterResponse.getAmount()));
            senderAccount.setAmount(senderAccount.getAmount().subtract(amountInSenderCurrency));
        }

        accountRepository.save(receiverAccount);
        accountRepository.save(senderAccount);

        webSocketController.sendAccountUpdate(senderAccount);
        webSocketController.sendAccountUpdate(receiverAccount);
    }
}
