package com.goldhouse.server.service;

import com.goldhouse.server.dto.CustomerRequestDTO;
import com.goldhouse.server.dto.CustomerResponseDTO;

import java.util.List;

public interface CustomerService {
    CustomerResponseDTO addCustomer(CustomerRequestDTO dto);

    boolean isCustomerAlreadyPresent(String name);

    CustomerResponseDTO getCustomer(String name);

    CustomerResponseDTO getCustomer(long customerId);

    List<CustomerResponseDTO> getAllCustomers();
}
