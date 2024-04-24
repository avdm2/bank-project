package ru.tinkoff.hse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tinkoff.hse.entities.Fee;

import java.util.Optional;
import java.util.UUID;

public interface FeeRepository extends JpaRepository<Fee, UUID> {
    Optional<Fee> findFirstByOrderByCreatedAtDesc();
}
