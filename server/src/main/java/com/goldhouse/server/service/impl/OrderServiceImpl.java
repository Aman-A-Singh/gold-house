package com.goldhouse.server.service.impl;

import com.goldhouse.server.dto.orderDTO.OrderRequestDTO;
import com.goldhouse.server.dto.orderDTO.OrderResponseDTO;
import com.goldhouse.server.exception.customException.ResourceNotFoundException;
import com.goldhouse.server.mapper.OrderMapper;
import com.goldhouse.server.model.Customer;
import com.goldhouse.server.model.Order;
import com.goldhouse.server.model.OrderStatus;
import com.goldhouse.server.repository.CustomerRepository;
import com.goldhouse.server.repository.OrderRepository;
import com.goldhouse.server.service.OrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final OrderMapper orderMapper;

    public OrderServiceImpl(OrderRepository orderRepository, CustomerRepository customerRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    public OrderResponseDTO addOrder(OrderRequestDTO orderRequestDTO) {
        Customer customer = customerRepository.findById(orderRequestDTO.getCustomer_id())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + orderRequestDTO.getCustomer_id()));
        Order order = orderMapper.toEntity(orderRequestDTO, customer);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toResponseDTO(savedOrder);
    }

    @Override
    public List<OrderResponseDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(orderMapper::toResponseDTO).toList();
    }

    @Override
    public void removeOrder(String orderId) {
        orderRepository.deleteById(orderId);
    }

    @Override
    public List<OrderResponseDTO> getOrdersByStatus(OrderStatus status) {
        List<Order> orderList = orderRepository.getOrderByOrderStatus(status);
        return orderList.stream().map(orderMapper::toResponseDTO).toList();
    }

    @Override
    public List<OrderResponseDTO> getOrdersByCustomerIdAndStatus(long customer_id, OrderStatus status) {
        List<Order> orderList = orderRepository.getOrdersByCustomerIdAndOrderStatus(customer_id, status);
        return orderList.stream().map(orderMapper::toResponseDTO).toList();
    }


    @Override
    public List<OrderResponseDTO> getOrdersByOrderDate(LocalDate date) {
        return orderRepository.getOrdersByOrderDate(date).stream().map(orderMapper::toResponseDTO).toList();
    }

    @Override
    public List<OrderResponseDTO> getOrdersByCustomerID(long customerId) {
        return orderRepository.getOrderByCustomerId(customerId).stream().map(orderMapper::toResponseDTO).toList();
    }

    @Override
    public OrderResponseDTO getOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
        return orderMapper.toResponseDTO(order);
    }


    @Override
    public long getTotalOrdersCount() {
        return orderRepository.count();
    }

    @Override
    public long getTotalOrdersCountByCustomerId(long customer_id) {
        return orderRepository.countByCustomerId(customer_id);
    }

    @Override
    public long getOrdersCountByStatus(OrderStatus status) {
        return orderRepository.countByOrderStatus(status);
    }

    @Override
    public long getOrdersCountByCustomerIdAndStatus(long customerId, OrderStatus status) {
        return orderRepository.countByCustomerIdAndOrderStatus(customerId, status);
    }

    @Override
    public OrderResponseDTO updateOrderDetails(OrderRequestDTO orderRequestDto) {
        Order order = orderRepository.getOrderByOrderDateAndOrderTime(
                orderRequestDto.getOrderDate(),
                orderRequestDto.getOrderTime()
        );

        if (order == null) {
            throw new ResourceNotFoundException("Order not found for the given date and time");
        }

        if (order.getOrderStatus().getValue() > orderRequestDto.getOrderStatus().getValue()) {
            throw new RuntimeException("Invalid Order Status update");
        }
        order.setDeliverDate(orderRequestDto.getDeliverDate());
        order.setDeliverTime(orderRequestDto.getDeliverTime());
        order.setOrderStatus(orderRequestDto.getOrderStatus());
        return orderMapper.toResponseDTO(orderRepository.save(order));
    }

    @Override
    public List<OrderResponseDTO> getTodaysPendingOrder(LocalDate today) {
        return orderRepository.getOrdersByOrderStatusAndOrderDate(OrderStatus.PENDING, today).stream().map(orderMapper::toResponseDTO).toList();
    }


    @Override
    public List<OrderResponseDTO> ordersDeliveredBetweenDate(LocalDate fromDate, LocalDate toDate) {
        return orderRepository.getOrdersByDeliverDateBetween(fromDate, toDate).stream().map(orderMapper::toResponseDTO).toList();
    }

    @Override
    public List<OrderResponseDTO> getOrdersByCustomerName(String customerName) {
        return orderRepository.getOrdersByCustomerName(customerName)
                .stream()
                .map(orderMapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<OrderResponseDTO> getOrdersByCustomerPhoneNumber(long customerPhoneNumber) {
        return orderRepository.getOrdersByCustomerPhoneNumber(customerPhoneNumber)
                .stream()
                .map(orderMapper::toResponseDTO)
                .toList();
    }
}
