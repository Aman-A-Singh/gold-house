package com.goldhouse.server.model;

import com.goldhouse.server.util.OrderId;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "orders")
@Data
public class Order {

    @Id
    @OrderId
    private String id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(nullable = false)
    private double weight;

    private double result;

    private double wastage;

    private int stampNo;

    @Column(nullable = false)
    private LocalDate orderDate;

    @Column(nullable = false)
    private LocalTime orderTime;

    private LocalDate deliverDate;

    private LocalTime deliverTime;

    private OrderStatus orderStatus = OrderStatus.PENDING;
}

