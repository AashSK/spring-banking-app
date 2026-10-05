package com.innobank.customer.dto;

import java.time.Instant;

import lombok.Builder;

@Builder
public record CustomerResponse(
        String firstName,
        String lastName,
        String email,
        Boolean active,
        Instant createdAt,
        Instant updatedAt) {
}