package ru.tinkoff.hse.repositories;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import ru.tinkoff.hse.entities.OutboxEvent;

import java.util.List;

public interface OutboxRepository extends JpaRepository<OutboxEvent, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select * from outboxes for update", nativeQuery = true)
    List<OutboxEvent> findAllAndLock();
}
