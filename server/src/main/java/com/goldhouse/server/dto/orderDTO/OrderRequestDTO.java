package com.goldhouse.server.dto.orderDTO;

import com.goldhouse.server.model.Customer;
import com.goldhouse.server.model.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;


@Data
public class OrderRequestDTO {
    private long customer_id;

    private double weight;

    private double result;

    private double wastage;

    private int stampNo;

    private LocalDate orderDate;

    private LocalTime orderTime;

    private LocalDate deliverDate;

    private LocalTime deliverTime;

    private OrderStatus orderStatus;
}
