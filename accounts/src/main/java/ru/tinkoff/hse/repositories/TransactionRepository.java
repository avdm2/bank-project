package ru.tinkoff.hse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tinkoff.hse.entities.Transaction;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
}
