package com.goldhouse.server.service.impl;

import com.goldhouse.server.dto.customerDTO.CustomerRequestDTO;
import com.goldhouse.server.dto.customerDTO.CustomerResponseDTO;
import com.goldhouse.server.exception.customException.ResourceNotFoundException;
import com.goldhouse.server.model.Customer;
import com.goldhouse.server.repository.CustomerRepository;
import com.goldhouse.server.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository; // Final ensures it never changes

    public CustomerServiceImpl(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public CustomerResponseDTO addCustomer(CustomerRequestDTO dto) {
        // 1. Create the Entity (Data Object)
        Customer customer = new Customer();
        // 2. Transfer data from the Request (DTO) to the Entity
        customer.setName(dto.getName());
        customer.setPhoneNumber(dto.getPhoneNumber());
        // 3. THIS IS THE MAGIC LINE
        // repository.save() automatically:
        // - Opens a connection
        // - Generates the INSERT SQL
        // - Handles the Sequence/ID generation (NEXT VALUE FOR...)
        // - Executes the transaction
        Customer savedCustomer = repository.save(customer);

        CustomerResponseDTO customerResponseDTO = new CustomerResponseDTO();
        customerResponseDTO.setId(savedCustomer.getId());
        customerResponseDTO.setName(savedCustomer.getName());
        customerResponseDTO.setPhoneNumber(savedCustomer.getPhoneNumber());
        return customerResponseDTO;
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
        CustomerResponseDTO customerResponseDTO = new CustomerResponseDTO();
        customerResponseDTO.setId(customer.getId());
        customerResponseDTO.setName(customer.getName());
        customerResponseDTO.setPhoneNumber(customer.getPhoneNumber());
        return customerResponseDTO;
    }

    @Override
    public CustomerResponseDTO getCustomer(long customerId) {
        // 1. Fetch & Unwrap (Throw error if missing)
        Customer customer = repository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + customerId));
        CustomerResponseDTO response = new CustomerResponseDTO();
        response.setId(customer.getId());
        response.setName(customer.getName());
        response.setPhoneNumber(customer.getPhoneNumber());
        return response;
    }

    @Override
    public List<CustomerResponseDTO> getAllCustomers() {
        var customerList = repository.findAll();
        var response = new ArrayList<CustomerResponseDTO>();
        customerList.forEach(customer -> {
            CustomerResponseDTO dto = new CustomerResponseDTO();
            dto.setId(customer.getId());
            dto.setName(customer.getName());
            dto.setPhoneNumber(customer.getPhoneNumber());
            response.add(dto);
        });
        return response;
    }
}