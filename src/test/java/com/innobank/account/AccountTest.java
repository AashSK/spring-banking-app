package com.innobank.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;

public class AccountTest {

    @Test
    void shouldCreateAccountWithPendingStatus() {
        Account account = new Account(
                UUID.randomUUID(),
                "ACC-000001",
                Account.AccountType.CURRENT);

        assertEquals(Account.AccountStatus.PENDING, account.getStatus());
        assertEquals(0L, account.getBalanceMinor());
        assertEquals("EUR", account.getCurrency());
        assertNull(account.getOpenedAt());
        assertNull(account.getClosedAt());
    }

    @Test
    void shouldRejectBlankAccountNumber() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Account(
                        UUID.randomUUID(),
                        " ",
                        Account.AccountType.CURRENT));
    }

    @Test
    void shouldRejectMissingCustomerId() {
        assertThrows(
                NullPointerException.class,
                () -> new Account(
                        null,
                        "ACC-000001",
                        Account.AccountType.CURRENT));
    }

    @Test
    void shouldRejectMissingAccountNumber() {
        assertThrows(
                NullPointerException.class,
                () -> new Account(
                        UUID.randomUUID(),
                        null,
                        Account.AccountType.CURRENT));
    }

    @Test
    void shouldRejectMissingAccountType() {
        assertThrows(
                NullPointerException.class,
                () -> new Account(
                        UUID.randomUUID(),
                        "ACC-000001",
                        null));
    }

    @Test
    void shouldActivatePendingAccount() {
        Account account = createAccount();

        account.activate();

        assertEquals(Account.AccountStatus.ACTIVE, account.getStatus());
        assertNotNull(account.getOpenedAt());
        assertNull(account.getClosedAt());
    }

    @Test
    void shouldRejectActivatingAccountTwice() {
        Account account = createAccount();
        account.activate();

        assertThrows(IllegalStateException.class, account::activate);
    }

    @Test
    void shouldFreezeActiveAccount() {
        Account account = createActiveAccount();

        account.freeze();

        assertEquals(Account.AccountStatus.FROZEN, account.getStatus());
    }

    @Test
    void shouldRejectFreezingPendingAccount() {
        Account account = createAccount();

        assertThrows(IllegalStateException.class, account::freeze);
    }

    @Test
    void shouldUnfreezeFrozenAccount() {
        Account account = createActiveAccount();
        account.freeze();

        account.unfreeze();

        assertEquals(Account.AccountStatus.ACTIVE, account.getStatus());
    }

    @Test
    void shouldRejectUnfreezingActiveAccount() {
        Account account = createActiveAccount();

        assertThrows(IllegalStateException.class, account::unfreeze);
    }

    @Test
    void shouldCloseActiveAccount() {
        Account account = createActiveAccount();

        account.close();

        assertEquals(Account.AccountStatus.CLOSED, account.getStatus());
        assertNotNull(account.getOpenedAt());
        assertNotNull(account.getClosedAt());
        assertFalse(account.getClosedAt().isBefore(account.getOpenedAt()));
    }

    @Test
    void shouldCloseFrozenAccount() {
        Account account = createActiveAccount();
        account.freeze();

        account.close();

        assertEquals(Account.AccountStatus.CLOSED, account.getStatus());
        assertNotNull(account.getClosedAt());
    }

    @Test
    void shouldRejectClosingPendingAccount() {
        Account account = createAccount();

        assertThrows(IllegalStateException.class, account::close);
    }

    @Test
    void shouldRejectClosingAccountTwice() {
        Account account = createActiveAccount();
        account.close();

        assertThrows(IllegalStateException.class, account::close);
    }

    private Account createAccount() {
        return new Account(
                UUID.randomUUID(),
                "ACC-000001",
                Account.AccountType.CURRENT);
    }

    private Account createActiveAccount() {
        Account account = createAccount();
        account.activate();
        return account;
    }

}
