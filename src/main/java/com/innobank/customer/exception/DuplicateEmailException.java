package com.innobank.customer.exception;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String email) {
        super("Customer with Provided Email:" + email + "already exists!");
    }
}
