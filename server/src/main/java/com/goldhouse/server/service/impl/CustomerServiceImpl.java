package com.goldhouse.server.service.impl;

import com.goldhouse.server.dto.customerDTO.CustomerRequestDTO;
import com.goldhouse.server.dto.customerDTO.CustomerResponseDTO;
import com.goldhouse.server.exception.customException.ResourceNotFoundException;
import com.goldhouse.server.mapper.CustomerMapper;
import com.goldhouse.server.model.Customer;
import com.goldhouse.server.repository.CustomerRepository;
import com.goldhouse.server.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository; // Final ensures it never changes
    private final CustomerMapper customerMapper;

    public CustomerServiceImpl(CustomerRepository repository, CustomerMapper customerMapper) {
        this.repository = repository;
        this.customerMapper = customerMapper;
    }

    @Override
    public CustomerResponseDTO addCustomer(CustomerRequestDTO dto) {

        Customer customer = customerMapper.toEntity(dto);
        Customer savedCustomer = repository.save(customer);
        return customerMapper.toResponseDTO(savedCustomer);
    }

    @Override
    public boolean isCustomerAlreadyPresent(String name) {
        return repository.existsByName(name);
    }

    @Override
    public CustomerResponseDTO getCustomer(String name) {
        Customer customer = repository.findByName(name);
        // Add this check!
        if (customer == null) {
            throw new ResourceNotFoundException("Customer not found with name: " + name);
        }
        return customerMapper.toResponseDTO(customer);
    }

    @Override
    public CustomerResponseDTO getCustomer(long customerId) {
        // 1. Fetch & Unwrap (Throw error if missing)
        Customer customer = repository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + customerId));
        return customerMapper.toResponseDTO(customer);
    }

    @Override
    public List<CustomerResponseDTO> getAllCustomers() {
        var customerList = repository.findAll();
        return customerMapper.toResponseDTOList(customerList);
    }
}