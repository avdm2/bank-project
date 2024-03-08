package ru.tinkoff.hse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tinkoff.hse.entities.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    List<Account> findAllByCustomerId(Integer customerId);
    Optional<Account> findByCustomerIdAndCurrency(Integer customerId, String currency);
    Optional<Account> findByAccountNumber(Integer accountNumber);
}
