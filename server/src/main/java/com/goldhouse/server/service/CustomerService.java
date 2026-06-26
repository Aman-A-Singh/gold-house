package com.goldhouse.server.service;

import com.goldhouse.server.dto.customerDTO.CustomerRequestDTO;
import com.goldhouse.server.dto.customerDTO.CustomerResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerService {
    CustomerResponseDTO addCustomer(CustomerRequestDTO dto);

    boolean isCustomerAlreadyPresent(String name, Long phoneNumber);

    CustomerResponseDTO getCustomer(String name);

    CustomerResponseDTO getCustomer(long customerId);

    List<CustomerResponseDTO> getAllCustomers();

    List<CustomerResponseDTO> searchCustomers(String query, Pageable pageable);
}
