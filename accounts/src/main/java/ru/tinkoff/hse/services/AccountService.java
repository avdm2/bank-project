package ru.tinkoff.hse.services;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.hse.dto.requests.AccountCreationRequest;
import ru.tinkoff.hse.dto.responses.AccountCreationResponse;
import ru.tinkoff.hse.dto.AccountMessage;
import ru.tinkoff.hse.dto.responses.GetAccountResponse;
import ru.tinkoff.hse.dto.requests.TopUpRequest;
import ru.tinkoff.hse.dto.requests.TransferRequest;
import ru.tinkoff.hse.entities.Account;
import ru.tinkoff.hse.entities.OutboxEvent;
import ru.tinkoff.hse.exceptions.ConverterGrpcException;
import ru.tinkoff.hse.lib.ConvertResponse;
import ru.tinkoff.hse.models.enums.OperationType;
import ru.tinkoff.hse.repositories.AccountRepository;
import ru.tinkoff.hse.repositories.OutboxRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final OutboxRepository outboxRepository;
    private final GrpcConverterClientService grpcConverterClientService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public AccountService(AccountRepository accountRepository,
                          OutboxRepository outboxRepository,
                          GrpcConverterClientService grpcConverterClientService,
                          SimpMessagingTemplate simpMessagingTemplate) {
        this.accountRepository = accountRepository;
        this.outboxRepository = outboxRepository;
        this.grpcConverterClientService = grpcConverterClientService;
        this.simpMessagingTemplate = simpMessagingTemplate;
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
        sendMessage(account);

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
        sendMessage(account);
        createOutboxEvent(account, OperationType.ADD, requestAmount);
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
        BigDecimal amountInReceiverCurrency = null;
        if (senderAccount.getAmount().compareTo(amountInSenderCurrency) < 0) {
            throw new IllegalArgumentException("amount to transfer > available funds");
        }

        if (senderAccount.getCurrency().equals(receiverAccount.getCurrency())) {
            receiverAccount.setAmount(receiverAccount.getAmount().add(amountInSenderCurrency));
            senderAccount.setAmount(senderAccount.getAmount().subtract(amountInSenderCurrency));
        } else {
            ConvertResponse converterResponse;
            try {
                converterResponse = grpcConverterClientService
                        .convert(senderAccount.getCurrency(), receiverAccount.getCurrency(), amountInSenderCurrency);
            } catch (Exception e) {
                throw new ConverterGrpcException("error with proceeding grpc request");
            }

            if (converterResponse == null) {
                throw new ConverterGrpcException("rates unavailable");
            }

            amountInReceiverCurrency = new BigDecimal(converterResponse.getConvertedAmount());
            receiverAccount.setAmount(receiverAccount.getAmount().add(amountInReceiverCurrency));
            senderAccount.setAmount(senderAccount.getAmount().subtract(amountInSenderCurrency));
        }

        accountRepository.save(receiverAccount);
        accountRepository.save(senderAccount);

        sendMessage(senderAccount);
        createOutboxEvent(senderAccount, OperationType.SUBTRACT, amountInSenderCurrency);
        sendMessage(receiverAccount);
        createOutboxEvent(receiverAccount, OperationType.ADD, amountInReceiverCurrency);
    }

    private void sendMessage(Account account) {
        AccountMessage accountMessage = new AccountMessage()
                .setAccountNumber(account.getAccountNumber())
                .setCurrency(account.getCurrency())
                .setBalance(account.getAmount().setScale(2, RoundingMode.HALF_EVEN));
        simpMessagingTemplate.convertAndSend("/topic/accounts", accountMessage);
    }

    private void createOutboxEvent(Account account, OperationType operation, BigDecimal amountToAdd) {
        String message = "Счет " + account.getAccountNumber() + ". Операция: " + operation + amountToAdd + ". Баланс: " + account.getAmount();
        outboxRepository.save(new OutboxEvent()
                .setCustomerId(account.getCustomerId())
                .setMessage(message));
    }
}