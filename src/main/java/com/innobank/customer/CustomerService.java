package com.innobank.customer;

import org.springframework.stereotype.Service;

import com.innobank.customer.dto.CustomerRequest;
import com.innobank.customer.dto.CustomerResponse;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerResponse createCustomer(CustomerRequest request) {

        Customer customer = customerRepository.save(new Customer(
                request.firstName(), request.lastName(), request.email()));
        // next get the customer by the uuid and return the customerResponse
    }

}
