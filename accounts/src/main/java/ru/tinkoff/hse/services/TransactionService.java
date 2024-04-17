package ru.tinkoff.hse.services;

import org.springframework.stereotype.Service;
import ru.tinkoff.hse.dto.responses.TransactionResponse;
import ru.tinkoff.hse.repositories.TransactionRepository;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<TransactionResponse> getAllTransactions(Integer accountNumber) {
        return transactionRepository.findAll()
                .stream()
                .filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
                .map(transaction -> new TransactionResponse()
                        .setTransactionId(transaction.getTransactionId())
                        .setAmount(transaction.getAmount()))
                .toList();
    }
}
