package com.goldhouse.server.controller;

import com.goldhouse.server.api.ApiResponse;
import com.goldhouse.server.dto.customerDTO.CustomerRequestDTO;
import com.goldhouse.server.dto.customerDTO.CustomerResponseDTO;
import com.goldhouse.server.service.CustomerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
@Validated
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CustomerResponseDTO>> addCustomer(@Valid @RequestBody CustomerRequestDTO dto) {
        CustomerResponseDTO customer = customerService.addCustomer(dto);
        return ResponseEntity.ok(
                ApiResponse.success(customer,"Customer added successfully")
        );
    }

    @GetMapping("/customer")
    public ResponseEntity<ApiResponse<CustomerResponseDTO>> getCustomer(
            @RequestParam @NotBlank(message = "Name cannot be empty") String name
    ) {
        CustomerResponseDTO customer = customerService.getCustomer(name);
        return ResponseEntity.ok(
                ApiResponse.success(customer,"Customer found")
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CustomerResponseDTO>>> getAllCustomers() {
        List<CustomerResponseDTO> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(
                ApiResponse.successWithCount(customers,"Customers found",customers.size())
        );
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<CustomerResponseDTO>>> searchCustomers(
            @RequestParam String query,
            @RequestParam(defaultValue = "10") int limit) {

        if (query.trim().isEmpty() || query.length() < 2) {
            return ResponseEntity.ok(
                    ApiResponse.successWithCount(Collections.emptyList(),"No Customers found",0)
            );
        }

        Pageable limitParams = PageRequest.of(0, limit);
        List<CustomerResponseDTO> suggestions = customerService.searchCustomers(query, limitParams);
        return ResponseEntity.ok(
                ApiResponse.successWithCount(suggestions,"Customers found",suggestions.size())
        );
    }
}
