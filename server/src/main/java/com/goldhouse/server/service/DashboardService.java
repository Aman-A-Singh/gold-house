package com.goldhouse.server.service;

import com.goldhouse.server.dto.dashboardDTO.DashboardResponseDTO;
import com.goldhouse.server.dto.orderDTO.OrderResponseDTO;
import com.goldhouse.server.model.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DashboardService {
    DashboardResponseDTO getDashboardStats(long userId);

    Page<OrderResponseDTO> getDashboardOrders(Pageable pageable,
                                              String customerName,
                                              Long customerPhoneNumber,
                                              String orderId,
                                              OrderStatus status,
                                              String sortBy,
                                              String sortDir);
}
