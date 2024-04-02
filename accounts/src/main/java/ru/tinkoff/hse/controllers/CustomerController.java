package ru.tinkoff.hse.controllers;

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
import ru.tinkoff.hse.services.RateLimiterService;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final RateLimiterService rateLimiterService;

    public CustomerController(CustomerService customerService, RateLimiterService rateLimiterService) {
        this.customerService = customerService;
        this.rateLimiterService = rateLimiterService;
    }

    @PostMapping
    public ResponseEntity<CustomerCreationResponse> create(@RequestBody CustomerCreationRequest request) {
        return ResponseEntity.ok().body(customerService.createCustomer(request));
    }

    @GetMapping("/{customerId}/balance")
    public ResponseEntity<GetTotalBalanceResponse> getTotalBalance(@PathVariable("customerId") Integer customerId,
                                                                   @RequestParam("currency") String currency) {

        try {
            rateLimiterService.getRateLimiterForCustomer(customerId).acquirePermission();
            GetTotalBalanceResponse response = customerService.getTotalBalanceInCurrency(customerId, currency);
            return ResponseEntity.ok(response);
        } catch (RequestNotPermitted exception) {
            throw new RateLimitExceededException("rate limit exceeded for customer " + customerId);
        }
    }
}
