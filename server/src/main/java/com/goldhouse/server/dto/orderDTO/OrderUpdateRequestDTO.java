package com.goldhouse.server.dto.orderDTO;

import com.goldhouse.server.annotation.deliveryDateValidator.ValidDateRange;
import com.goldhouse.server.dto.customerDTO.CustomerRequestDTO;
import com.goldhouse.server.model.OrderStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@ValidDateRange(
        startDateField = "orderDate",
        endDateField = "deliverDate",
        message = "Delivery date must be after or equal to the order date"
)
public class OrderUpdateRequestDTO {
    @NotNull(message = "Customer is required")
    private CustomerRequestDTO customer;

    @NotNull(message = "Weight is required")
    @Positive(message = "Weight must be greater than 0")
    private double weight;

    @Positive(message = "Result must be greater than 0")
    private double result;

    @Min(value = 0, message = "Wastage cannot be negative")
    private double wastage;

    @Min(value = 1, message = "Stamp number must be at least 1")
    private int stampNo;

    @NotNull(message = "Order date is required")
    private LocalDate orderDate;

    @NotNull(message = "Order time is required")
    private LocalTime orderTime;

    private LocalDate deliverDate;

    private LocalTime deliverTime;

    @NotNull(message = "Order status is required")
    private OrderStatus orderStatus;
}
