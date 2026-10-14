package com.innobank.account;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import com.innobank.customer.Customer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    public enum AccountType {
        CURRENT,
        SAVINGS
    }

    public enum AccountStatus {
        PENDING,
        ACTIVE,
        FROZEN,
        CLOSED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "account_id")
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Setter(AccessLevel.NONE)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false, unique = true, updatable = false)
    private Customer customer;

    @Setter(AccessLevel.NONE)
    @Column(name = "account_number", nullable = false, unique = true, length = 34)
    private String accountNumber;

    @Setter(AccessLevel.NONE)
    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false, length = 7)
    private AccountType accountType;

    @Setter(AccessLevel.NONE)
    @Column(name = "currency", nullable = false, length = 3)
    private String currency = "EUR";

    @Setter(AccessLevel.NONE)
    @Column(name = "balance_minor", nullable = false)
    private long balanceMinor = 0L;

    @Setter(AccessLevel.NONE)
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 7)
    private AccountStatus status = AccountStatus.PENDING;

    @Setter(AccessLevel.NONE)
    @Column(name = "opened_at")
    private Instant openedAt;

    @Setter(AccessLevel.NONE)
    @Column(name = "closed_at")
    private Instant closedAt;

    @Setter(AccessLevel.NONE)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Setter(AccessLevel.NONE)
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public Account(Customer customer, String accountNumber, AccountType accountType) {
        this.customer = Objects.requireNonNull(customer, "Customer is required");
        this.accountNumber = Objects.requireNonNull(accountNumber, "Account number is required");
        this.accountType = Objects.requireNonNull(accountType, "Account type is required");

        if (accountNumber.isBlank() || accountNumber.length() > 34) {
            throw new IllegalArgumentException(
                    "Account number must contain between 1 and 34 characters");
        }

        /* Use this Code only if currency is manually to be set
        if (!currency.matches("[A-Z]{3}")) {
            throw new IllegalArgumentException("Currency must be a three-letter uppercase code");
        } */

    }

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    public void activate() {
        if (status != AccountStatus.PENDING) {
            throw new IllegalStateException("Only pending accounts can be activated");
        }
        status = AccountStatus.ACTIVE;
        openedAt = Instant.now();
    }

    public void freeze() {
        if (status != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Only active accounts can be frozen");
        }
        status = AccountStatus.FROZEN;
    }

    public void close() {
        if (status != AccountStatus.ACTIVE && status != AccountStatus.FROZEN) {
            throw new IllegalStateException("Only active or frozen accounts can be closed");
        }
        status = AccountStatus.CLOSED;
        closedAt = Instant.now();
    }

}
