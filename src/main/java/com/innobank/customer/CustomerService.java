package com.innobank.customer;

import org.springframework.stereotype.Service;

import com.innobank.customer.dto.CustomerRequest;
import com.innobank.customer.dto.CustomerResponse;
import com.innobank.customer.exception.DuplicateEmailException;
import com.innobank.customer.mapper.CustomerMapper;

import jakarta.transaction.Transactional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Transactional
    public CustomerResponse createCustomer(CustomerRequest request) {

        var existingCustomer = customerRepository.findByEmail(request.email());

        if (existingCustomer.isPresent()) {
            throw new DuplicateEmailException(request.email());
        }

        Customer customer = customerRepository.save(customerMapper.toEntity(request));

        return customerMapper.toResponse(customer);
    }

}
