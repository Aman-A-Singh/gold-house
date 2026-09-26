package com.goldhouse.server.dto.customerDTO;

public record CustomerOrderSummaryDTO(
        Long id,
        String name,
        Long phoneNumber,
        long totalOrders
) {}
