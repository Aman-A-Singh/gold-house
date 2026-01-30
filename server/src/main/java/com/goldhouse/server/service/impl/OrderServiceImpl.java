package com.goldhouse.server.service.impl;

import com.goldhouse.server.dto.orderDTO.OrderRequestDTO;
import com.goldhouse.server.dto.orderDTO.OrderResponseDTO;
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

    public OrderServiceImpl(OrderRepository orderRepository, CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public OrderResponseDTO addOrder(OrderRequestDTO orderRequestDTO) {
        Customer customer = customerRepository.findById(orderRequestDTO.getCustomer_id())
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + orderRequestDTO.getCustomer_id()));
        Order order = mapRequestDTO(orderRequestDTO, customer);
        Order orderResponse = orderRepository.save(order);
        return mapOrderToResponseDTO(orderResponse);
    }

    @Override
    public List<OrderResponseDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(this::mapOrderToResponseDTO).toList();
    }

    @Override
    public void removeOrder(String orderId) {
        orderRepository.deleteById(orderId);
    }

    @Override
    public List<OrderResponseDTO> getOrdersByStatus(OrderStatus status) {
        List<Order> orderList = orderRepository.getOrderByOrderStatus(status);
        return orderList.stream().map(this::mapOrderToResponseDTO).toList();
    }

    @Override
    public List<OrderResponseDTO> getOrdersByCustomerIdAndStatus(long customer_id, OrderStatus status) {
        List<Order> orderList = orderRepository.getOrdersByCustomerIdAndOrderStatus(customer_id, status);
        return orderList.stream().map(this::mapOrderToResponseDTO).toList();
    }


    @Override
    public List<OrderResponseDTO> getOrdersByOrderDate(LocalDate date) {
        return orderRepository.getOrdersByOrderDate(date).stream().map(this::mapOrderToResponseDTO).toList();
    }

    @Override
    public List<OrderResponseDTO> getOrdersByCustomerID(long customerId) {
        return orderRepository.getOrderByCustomerId(customerId).stream().map(this::mapOrderToResponseDTO).toList();
    }

    @Override
    public OrderResponseDTO getOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
        return mapOrderToResponseDTO(order);
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
            throw new RuntimeException("Order not found for the given date and time");
        }

        if(order.getOrderStatus().getValue() > orderRequestDto.getOrderStatus().getValue()){
            throw new RuntimeException("Invalid Order Status update");
        }
        order.setDeliverDate(orderRequestDto.getDeliverDate());
        order.setDeliverTime(orderRequestDto.getDeliverTime());
        order.setOrderStatus(orderRequestDto.getOrderStatus());
        return mapOrderToResponseDTO(orderRepository.save(order));
    }

    @Override
    public List<OrderResponseDTO> getTodaysPendingOrder(LocalDate today) {
        return orderRepository.getOrdersByOrderStatusAndOrderDate(OrderStatus.PENDING, today).stream().map(this::mapOrderToResponseDTO).toList();
    }


    @Override
    public List<OrderResponseDTO> ordersDeliveredBetweenDate(LocalDate fromDate, LocalDate toDate) {
        return orderRepository.getOrdersByDeliverDateBetween(fromDate, toDate).stream().map(this::mapOrderToResponseDTO).toList();
    }

    @Override
    public List<OrderResponseDTO> getOrdersByCustomerName(String customerName) {
        return orderRepository.getOrdersByCustomerName(customerName)
                .stream()
                .map(this::mapOrderToResponseDTO)
                .toList();
    }

    @Override
    public List<OrderResponseDTO> getOrdersByCustomerPhoneNumber(long customerPhoneNumber) {
        return orderRepository.getOrdersByCustomerPhoneNumber(customerPhoneNumber)
                .stream()
                .map(this::mapOrderToResponseDTO)
                .toList();
    }

    private Order mapRequestDTO(OrderRequestDTO orderRequestDTO, Customer customer) {
        Order order = new Order();
        order.setCustomer(customer);
        order.setWeight(orderRequestDTO.getWeight());
        order.setResult(orderRequestDTO.getResult());
        order.setWastage(orderRequestDTO.getWastage());
        order.setStampNo(orderRequestDTO.getStampNo());

        order.setOrderDate(orderRequestDTO.getOrderDate());
        order.setOrderTime(orderRequestDTO.getOrderTime());
        order.setDeliverDate(orderRequestDTO.getDeliverDate());
        order.setDeliverTime(orderRequestDTO.getDeliverTime());
        order.setOrderStatus(orderRequestDTO.getOrderStatus());
        return order;
    }

    private OrderResponseDTO mapOrderToResponseDTO(Order order) {
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setOrderId(order.getId());
        orderResponseDTO.setCustomer(order.getCustomer());
        orderResponseDTO.setWeight(order.getWeight());
        orderResponseDTO.setResult(order.getResult());
        orderResponseDTO.setWastage(order.getWastage());
        orderResponseDTO.setStampNo(order.getStampNo());
        orderResponseDTO.setOrderDate(order.getOrderDate());
        orderResponseDTO.setOrderTime(order.getOrderTime());
        orderResponseDTO.setDeliverDate(order.getDeliverDate());
        orderResponseDTO.setDeliverTime(order.getDeliverTime());
        orderResponseDTO.setOrderStatus(order.getOrderStatus());

        return orderResponseDTO;
    }
}
