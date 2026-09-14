package com.goldhouse.server.repository;

import com.goldhouse.server.model.Order;
import com.goldhouse.server.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String>, JpaSpecificationExecutor<Order> {
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

    @Query("SELECT o FROM Order o WHERE o.orderStatus = com.goldhouse.server.model.OrderStatus.PENDING OR o.orderDate = :today ORDER BY o.orderDate DESC, o.orderTime DESC")
    List<Order> findPendingAndTodayOrders(@Param("today") LocalDate today);
}
