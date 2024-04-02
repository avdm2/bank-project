package ru.tinkoff.hse.controllers;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.hse.dto.CustomerCreationRequest;
import ru.tinkoff.hse.dto.CustomerCreationResponse;
import ru.tinkoff.hse.dto.GetTotalBalanceResponse;
import ru.tinkoff.hse.exceptions.RateLimitExceededException;
import ru.tinkoff.hse.services.CustomerService;

import java.util.function.Supplier;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final RateLimiterRegistry rateLimiterRegistry;

    public CustomerController(CustomerService customerService, RateLimiterRegistry rateLimiterRegistry) {
        this.customerService = customerService;
        this.rateLimiterRegistry = rateLimiterRegistry;
    }

    @PostMapping
    public ResponseEntity<CustomerCreationResponse> create(@RequestBody CustomerCreationRequest request) {
        return ResponseEntity.ok().body(customerService.createCustomer(request));
    }

    @GetMapping("/{customerId}/balance")
    public ResponseEntity<GetTotalBalanceResponse> getTotalBalance(@PathVariable("customerId") Integer customerId,
                                                                   @RequestParam("currency") String currency) {
        RateLimiter rateLimiter = rateLimiterRegistry.rateLimiter("customerRateLimiter");
        Supplier<GetTotalBalanceResponse> restrictedSupplier = RateLimiter.decorateSupplier(rateLimiter,
                () -> customerService.getTotalBalanceInCurrency(customerId, currency));

        try {
            return ResponseEntity.ok(restrictedSupplier.get());
        } catch (RequestNotPermitted exception) {
            throw new RateLimitExceededException("rate limit exceeded for customer " + customerId);
        }
    }
}
