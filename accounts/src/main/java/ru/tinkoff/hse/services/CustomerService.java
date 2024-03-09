package ru.tinkoff.hse.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
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
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;

    @Value("${app.converter-url}")
    private String converterUrl;

    public CustomerService(CustomerRepository customerRepository, AccountRepository accountRepository) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
    }

    public CustomerCreationResponse createCustomer(CustomerCreationRequest request) {
        if (request.getBirthDay() == null || request.getFirstName() == null || request.getLastName() == null) {
            throw new IllegalArgumentException("check required fields");
        }

        validateBirthdateOrElseThrow(request.getBirthDay());

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

        RestTemplate restTemplate = new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(10))
                .setReadTimeout(Duration.ofSeconds(10))
                .build();

        BigDecimal balance = BigDecimal.ZERO;
        for (Account account : accountList) {
            String requestUrl = converterUrl +
                    "?from=" + account.getCurrency() +
                    "&to=" + currency +
                    "&amount=" + account.getAmount();
            ConverterResponse converterResponse = restTemplate.getForEntity(requestUrl, ConverterResponse.class).getBody();
            if (converterResponse == null) {
                throw new NullPointerException("error with gotten response from converter");
            }
            balance = balance.add(converterResponse.getAmount());
        }

        return new GetTotalBalanceResponse().setBalance(balance).setCurrency(currency);
    }

    private void validateBirthdateOrElseThrow(String birthdate) {
        LocalDate birthday;
        try {
            birthday = LocalDate.parse(birthdate);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("cannot parse birthdate format");
        }

        if (birthday.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("illegal birthdate");
        }

        int age = Period.between(birthday, LocalDate.now()).getYears();
        if (age < 14 || age > 120) {
            throw new IllegalArgumentException("invalid age");
        }
    }
}
