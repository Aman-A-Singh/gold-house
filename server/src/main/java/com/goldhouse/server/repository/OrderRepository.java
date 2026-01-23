package com.goldhouse.server.repository;

import com.goldhouse.server.model.Order;
import com.goldhouse.server.model.OrderStatus;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> getOrdersByCustomerIdAndOrderStatus(long customer_id, OrderStatus status);
    List<Order> getOrderByOrderStatus(OrderStatus status);
    List<Order> getOrderByCustomerId(long customer_id);
    long countByCustomerId(long customer_id);

    long countByOrderStatus(OrderStatus status);
    long countByCustomerIdAndOrderStatus(long customerId, OrderStatus status);

    List<Order> getOrdersByCustomerName(String customerName);
    List<Order> getOrdersByCustomerPhoneNumber(long customerPhoneNumber);

    List<Order> getOrdersByOrderDate(LocalDate date);

    List<Order> getOrdersByDeliverDateBetween(LocalDate fromDate, LocalDate toDate);

    List<Order> getOrdersByOrderStatusAndOrderDate(OrderStatus status, LocalDate date);

    Order getOrderByOrderDateAndOrderTime(LocalDate date, LocalTime time);
}
