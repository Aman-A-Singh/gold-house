package com.goldhouse.server.repository;

import com.goldhouse.server.dto.customerDTO.CustomerOrderSummaryDTO;
import com.goldhouse.server.model.Customer;
import org.springframework.data.domain.Page;
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

    Customer findByNameAndPhoneNumber(String name, Long phoneNumber);

    @Query("SELECT c FROM Customer c WHERE LOWER(c.name) LIKE LOWER(concat('%', :query, '%'))")
    List<Customer> searchCustomers(@Param("query") String query, Pageable pageable);

    // Fetch summary for all customers
    @Query("""
                SELECT new com.goldhouse.server.dto.customerDTO.CustomerOrderSummaryDTO(
                    c.id, c.name, c.phoneNumber, COUNT(o)
                )
                FROM Customer c
        
                LEFT JOIN Order o ON o.customer.id = c.id
                            WHERE (:query IS NULL OR :query = '' OR LOWER(c.name) LIKE LOWER(concat('%', :query, '%')))
                GROUP BY c.id, c.name, c.phoneNumber
                  ORDER BY
                                c.id ASC
            """)
    Page<CustomerOrderSummaryDTO> findAllCustomerSummaries(Pageable pageable, @Param("query") String query);

}