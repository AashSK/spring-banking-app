package com.innobank.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

import com.innobank.PgTestConfiguration;
import com.innobank.customer.Customer;
import com.innobank.customer.CustomerRepository;

@SpringBootTest
@Import(PgTestConfiguration.class)
public class AccountRepositoryIntegrationTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        // Accounts reference customers, so delete accounts first.
        accountRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    void shouldPersistAccountForExistingCustomer() {
        Customer customer = customerRepository.save(createValidCustomer());

        Account account = new Account(
                customer.getId(),
                "ACC-000001",
                Account.AccountType.CURRENT);

        Account saved = accountRepository.saveAndFlush(account);

        assertNotNull(saved.getId());
        assertEquals(customer.getId(), saved.getCustomerId());
        assertEquals(Account.AccountStatus.PENDING, saved.getStatus());

        assertTrue(accountRepository.findById(saved.getId()).isPresent());
    }

    @Test
    void shouldRejectAccountForNonexistentCustomer() {
        Account account = new Account(
                UUID.randomUUID(),
                "ACC-000002",
                Account.AccountType.CURRENT);

        assertThrows(
                DataIntegrityViolationException.class,
                () -> accountRepository.saveAndFlush(account));
    }

    @Test
    void shouldAllowMultipleAccountsForSameCustomer() {
        Customer customer = customerRepository.save(createValidCustomer());

        Account firstAccount = new Account(
                customer.getId(),
                "ACC-000003",
                Account.AccountType.CURRENT);

        Account secondAccount = new Account(
                customer.getId(),
                "ACC-000004",
                Account.AccountType.SAVINGS);

        accountRepository.saveAndFlush(firstAccount);
        accountRepository.saveAndFlush(secondAccount);

        assertEquals(2, accountRepository.count());
    }

    @Test
    void shouldRejectDuplicateAccountNumber() {
        Customer customer = customerRepository.save(createValidCustomer());

        Account firstAccount = new Account(
                customer.getId(),
                "ACC-000005",
                Account.AccountType.CURRENT);

        Account duplicateAccount = new Account(
                customer.getId(),
                "ACC-000005",
                Account.AccountType.SAVINGS);

        accountRepository.saveAndFlush(firstAccount);

        assertThrows(
                DataIntegrityViolationException.class,
                () -> accountRepository.saveAndFlush(duplicateAccount));
    }

    private Customer createValidCustomer() {
        // Replace this with the constructor required by your Customer entity.
        return new Customer(
                "John",
                "Doe",
                "john@example.com");
    }
}
