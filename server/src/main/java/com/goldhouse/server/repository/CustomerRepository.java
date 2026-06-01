package com.goldhouse.server.repository;

import com.goldhouse.server.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// JpaRepository gives you save(), findAll(), findById(), deleteById() for free!
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // You can define custom queries here if needed
    boolean existsByName(String name);

    boolean existsByNameAndPhoneNumber(String name, Long phoneNumber);

    Customer findByName(String name);

    Customer findByNameAndPhoneNumber(String  name, Long phoneNumber);
}