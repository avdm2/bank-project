package ru.tinkoff.hse.controllers;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
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
import ru.tinkoff.hse.services.CustomerService;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerCreationResponse> create(@RequestBody CustomerCreationRequest request) {
        return ResponseEntity.ok().body(customerService.createCustomer(request));
    }

    @GetMapping("/{customerId}/balance")
    @RateLimiter(name = "customerBalanceRateLimiter", fallbackMethod = "rateLimitFallback")
    public ResponseEntity<GetTotalBalanceResponse> getTotalBalance(@PathVariable("customerId") Integer customerId,
                                                                   @RequestParam("currency") String currency) {
        return ResponseEntity.ok().body(customerService.getTotalBalanceInCurrency(customerId, currency));
    }

    public ResponseEntity<String> rateLimitFallback(Long customerId, Throwable t) {
        return ResponseEntity.status(500).body("rate limit exceeded for customer " + customerId);
    }
}
