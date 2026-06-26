package com.goldhouse.server.repository;

import com.goldhouse.server.model.Customer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

// JpaRepository gives you save(), findAll(), findById(), deleteById() for free!
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // You can define custom queries here if needed
    boolean existsByName(String name);

    boolean existsByNameAndPhoneNumber(String name, Long phoneNumber);

    Customer findByName(String name);

    Customer findByNameAndPhoneNumber(String  name, Long phoneNumber);

    @Query("SELECT c FROM Customer c WHERE LOWER(c.name) LIKE LOWER(concat('%', :query, '%'))")
    List<Customer> searchCustomers(@Param("query") String query, Pageable pageable);

}