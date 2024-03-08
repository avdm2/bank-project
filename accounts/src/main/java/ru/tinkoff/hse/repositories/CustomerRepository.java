package ru.tinkoff.hse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tinkoff.hse.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
