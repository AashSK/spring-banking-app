package com.innobank.customer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CustomerRequest(
        @NotBlank @Size(min = 4, max = 50, message = "Firstname must be between 4 and 50 characters") String firstName,
        @NotBlank @Size(min = 4, max = 50, message = "Lastname must be between 4 and 50 characters") String lastName,
        @NotBlank @Email(message = "Invalid email address") String email) {
}
