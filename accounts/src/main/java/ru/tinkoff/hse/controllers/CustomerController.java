package ru.tinkoff.hse.controllers;

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

    @PostMapping()
    public ResponseEntity<CustomerCreationResponse> create(@RequestBody CustomerCreationRequest request) {
        return ResponseEntity.ok().body(customerService.createCustomer(request));
    }

    @GetMapping("/{customerId}/balance")
    public ResponseEntity<GetTotalBalanceResponse> getTotalBalance(@PathVariable Integer customerId,
                                                                   @RequestParam String currency) {
        return ResponseEntity.ok().body(customerService.getTotalBalanceInCurrency(customerId, currency));
    }
}