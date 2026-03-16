package com.goldhouse.server.service;

import com.goldhouse.server.dto.homeDTO.HomeResponseDTO;
import com.goldhouse.server.dto.orderDTO.OrderRequestDTO;
import com.goldhouse.server.dto.orderDTO.OrderResponseDTO;
import com.goldhouse.server.model.OrderStatus;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {

    OrderResponseDTO addOrder(OrderRequestDTO orderRequestDTO);

    List<OrderResponseDTO> getAllOrders();

    void removeOrder(String orderId);

    List<OrderResponseDTO> getOrdersByCustomerIdAndStatus(long customer_id, OrderStatus status);

    List<OrderResponseDTO> getOrdersByStatus(OrderStatus status);

    List<OrderResponseDTO> getOrdersByOrderDate(LocalDate date);

    List<OrderResponseDTO> getOrdersByCustomerID(long customerId);

    OrderResponseDTO getOrder(String orderId);


    long getTotalOrdersCount();

    long getTotalOrdersCountByCustomerId(long customer_id);

    long getOrdersCountByStatus(OrderStatus status);

    long getOrdersCountByCustomerIdAndStatus(long customerId, OrderStatus status);

    OrderResponseDTO updateOrderDetails(OrderRequestDTO orderRequestDto);

    List<OrderResponseDTO> getTodaysPendingOrder(LocalDate today);

    List<OrderResponseDTO> ordersDeliveredBetweenDate(LocalDate fromDate, LocalDate toDate);

    List<OrderResponseDTO> getOrdersByCustomerName(String customerName);

    List<OrderResponseDTO> getOrdersByCustomerPhoneNumber(long customerPhoneNumber);

    HomeResponseDTO getHomeData(long userId);
}
