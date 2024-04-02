package ru.tinkoff.hse.services;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {

    private final Map<Integer, Bucket> buckets = new ConcurrentHashMap<>();

    public Bucket getBucket(int customerId) {
        return buckets.computeIfAbsent(customerId, this::createNewBucket);
    }

    private Bucket createNewBucket(int customerId) {
        Bandwidth limit = Bandwidth.builder().capacity(5).refillIntervally(5, Duration.ofSeconds(60)).build();
        return Bucket.builder().addLimit(limit).build();
    }
}
