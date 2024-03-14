package ru.tinkoff.hse.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.tinkoff.hse.dto.ConverterResponse;
import ru.tinkoff.hse.dto.GetTotalBalanceResponse;
import ru.tinkoff.hse.dto.CustomerCreationRequest;
import ru.tinkoff.hse.dto.CustomerCreationResponse;
import ru.tinkoff.hse.entities.Account;
import ru.tinkoff.hse.entities.Customer;
import ru.tinkoff.hse.repositories.AccountRepository;
import ru.tinkoff.hse.repositories.CustomerRepository;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final KeycloakTokenRequestService keycloakTokenRequestService;

    @Value("${app.converter-url}")
    private String converterUrl;

    public CustomerService(CustomerRepository customerRepository,
                           AccountRepository accountRepository,
                           KeycloakTokenRequestService keycloakTokenRequestService) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.keycloakTokenRequestService = keycloakTokenRequestService;
    }

    public CustomerCreationResponse createCustomer(CustomerCreationRequest request) {
        if (request.getBirthDay() == null || request.getFirstName() == null || request.getLastName() == null) {
            throw new IllegalArgumentException("check required fields");
        }

        Customer customer = new Customer()
                .setFirstname(request.getFirstName())
                .setLastname(request.getLastName())
                .setBirthday(request.getBirthDay());
        customerRepository.save(customer);

        return new CustomerCreationResponse().setCustomerId(customer.getId());
    }

    public GetTotalBalanceResponse getTotalBalanceInCurrency(Integer customerId, String currency) {
        if (customerId == null || currency == null) {
            throw new IllegalArgumentException("check required fields");
        }

        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        if (customerOptional.isEmpty()) {
            throw new IllegalArgumentException("customer with such id not found");
        }

        List<Account> accountList = accountRepository.findAllByCustomerId(customerId);
        if (accountList.isEmpty()) {
            throw new IllegalArgumentException("accounts for customer with such id not found");
        }



        BigDecimal balance = BigDecimal.ZERO;
        for (Account account : accountList) {
            String token = keycloakTokenRequestService.getToken();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            RestTemplate restTemplate = new RestTemplateBuilder()
                    .setConnectTimeout(Duration.ofSeconds(10))
                    .setReadTimeout(Duration.ofSeconds(10))
                    .build();

            String requestUrl = converterUrl + "/convert/" +
                    "?from=" + account.getCurrency() +
                    "&to=" + currency +
                    "&amount=" + account.getAmount();
            ConverterResponse converterResponse = restTemplate
                    .getForEntity(requestUrl, ConverterResponse.class, new HttpEntity<>(null, headers))
                    .getBody();
            if (converterResponse == null) {
                throw new NullPointerException("error with gotten response from converter");
            }
            balance = balance.add(converterResponse.getAmount());
        }

        return new GetTotalBalanceResponse().setBalance(balance).setCurrency(currency);
    }
}
