package com.goldhouse.server.service.impl;

import com.goldhouse.server.dto.homeDTO.HomeMetrics;
import com.goldhouse.server.dto.homeDTO.HomeResponseDTO;
import com.goldhouse.server.dto.orderDTO.OrderRequestDTO;
import com.goldhouse.server.dto.orderDTO.OrderResponseDTO;
import com.goldhouse.server.dto.user.UserDTO;
import com.goldhouse.server.exception.customException.ResourceNotFoundException;
import com.goldhouse.server.mapper.CustomerMapper;
import com.goldhouse.server.mapper.OrderMapper;
import com.goldhouse.server.model.Customer;
import com.goldhouse.server.model.Order;
import com.goldhouse.server.model.OrderStatus;
import com.goldhouse.server.model.User;
import com.goldhouse.server.repository.CustomerRepository;
import com.goldhouse.server.repository.OrderRepository;
import com.goldhouse.server.repository.UserRepository;
import com.goldhouse.server.repository.specification.OrderSpecifications;
import com.goldhouse.server.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final CustomerMapper customerMapper;

    public OrderServiceImpl(OrderRepository orderRepository, CustomerRepository customerRepository,
            OrderMapper orderMapper, UserRepository userRepository, CustomerMapper customerMapper) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.orderMapper = orderMapper;
        this.customerMapper = customerMapper;
    }

    @Override
    public OrderResponseDTO addOrder(OrderRequestDTO orderRequestDTO) {
        Customer customer = null;
        if (orderRequestDTO.getCustomer().getId() != null) {
            customer = customerRepository.findById(orderRequestDTO.getCustomer().getId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Customer not found with ID: " + orderRequestDTO.getCustomer().getId()));
        } else {
            Customer customerEntity = customerMapper.toEntity(orderRequestDTO.getCustomer());
            customer = customerRepository.save(customerEntity);
        }

        Order order = orderMapper.toEntity(orderRequestDTO, customer);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toResponseDTO(savedOrder);
    }

    @Override
    public Page<OrderResponseDTO> getAllOrders(Pageable pageable) {
        Page<Order> orderPage = orderRepository.findAll(pageable);
        return orderPage.map(orderMapper::toResponseDTO);
    }

    @Override
    public Page<OrderResponseDTO> getFilteredOrders(
            Pageable pageable,
            String customerName,
            Long customerPhoneNumber,
            String orderId,
            OrderStatus status,
            String sortBy,
            String sortDir) {
        // Define sorting direction safely
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageableWithSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        // Combine all dynamic specifications
        Specification<Order> spec = OrderSpecifications.filterOrders(orderId, customerName, customerPhoneNumber,
                status);
        Page<Order> orderPage = orderRepository.findAll(spec, pageableWithSort);
        return orderPage.map(orderMapper::toResponseDTO);
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
                orderRequestDto.getOrderTime());

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
        return orderRepository.getOrdersByOrderStatusAndOrderDate(OrderStatus.PENDING, today).stream()
                .map(orderMapper::toResponseDTO).toList();
    }


    @Override
    public List<OrderResponseDTO> ordersDeliveredBetweenDate(LocalDate fromDate, LocalDate toDate) {
        return orderRepository.getOrdersByDeliverDateBetween(fromDate, toDate).stream().map(orderMapper::toResponseDTO)
                .toList();
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

    @Override
    public HomeResponseDTO getHomeData(long userId) {
        long totalOrders = orderRepository.count();
        long pendingOrders = orderRepository.countByOrderStatus(OrderStatus.PENDING);
        long deliveredOrders = orderRepository.countByOrderStatus(OrderStatus.DELIVERED);
        long canceledOrders = orderRepository.countByOrderStatus(OrderStatus.CANCELLED);

        List<Order> orders = orderRepository.findAll();
        List<OrderResponseDTO> recentOrders = orders.stream().map(orderMapper::toResponseDTO).toList();

        User user = userRepository.findUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found after authentication"));

        UserDTO userDTO = UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();

        HomeMetrics metrics = HomeMetrics.builder()
                .totalOrders(totalOrders)
                .pendingOrders(pendingOrders)
                .deliveredOrders(deliveredOrders)
                .canceledOrders(canceledOrders)
                .build();

        return HomeResponseDTO.builder()
                .user(userDTO)
                .metrics(metrics)
                .recentOrders(recentOrders)
                .build();
    }
}
