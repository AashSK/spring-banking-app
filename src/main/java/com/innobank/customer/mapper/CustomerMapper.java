package com.innobank.customer.mapper;

import org.springframework.stereotype.Component;

import com.innobank.customer.Customer;
import com.innobank.customer.dto.CustomerRequest;
import com.innobank.customer.dto.CustomerResponse;

@Component
public class CustomerMapper {

    public Customer toEntity(CustomerRequest req) {
        return new Customer(req.firstName(), req.lastName(), req.email());
    }

    public CustomerResponse toResponse(Customer c) {
        var res = new CustomerResponse(
                c.getId(),
                c.getFirstName(),
                c.getLastName(),
                c.getEmail(),
                c.isActive(),
                c.getCreatedAt(),
                c.getUpdatedAt());
        return res;
    }

}
