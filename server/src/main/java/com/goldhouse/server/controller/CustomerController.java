package com.goldhouse.server.controller;

import com.goldhouse.server.dto.customerDTO.CustomerRequestDTO;
import com.goldhouse.server.dto.customerDTO.CustomerResponseDTO;
import com.goldhouse.server.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> addCustomer(@RequestBody CustomerRequestDTO dto) {
        return new ResponseEntity<>(customerService.addCustomer(dto), HttpStatus.CREATED);
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> isCustomerAlreadyPresent(@RequestParam String name) {
        boolean exists = customerService.isCustomerAlreadyPresent(name);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/customer")
    public ResponseEntity<CustomerResponseDTO> getCustomer(@RequestParam String name) {
        return new ResponseEntity<>(customerService.getCustomer(name), HttpStatus.OK);
    }

    @GetMapping
    public List<CustomerResponseDTO> getAllCustomers() {
       return customerService.getAllCustomers();
    }
}
