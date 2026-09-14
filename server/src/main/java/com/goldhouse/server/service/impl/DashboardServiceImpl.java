package com.goldhouse.server.service.impl;

import com.goldhouse.server.dto.dashboardDTO.DashboardMetrics;
import com.goldhouse.server.dto.dashboardDTO.DashboardResponseDTO;
import com.goldhouse.server.dto.orderDTO.OrderResponseDTO;
import com.goldhouse.server.dto.user.UserDTO;
import com.goldhouse.server.mapper.OrderMapper;
import com.goldhouse.server.model.Order;
import com.goldhouse.server.model.OrderStatus;
import com.goldhouse.server.model.User;
import com.goldhouse.server.repository.OrderRepository;
import com.goldhouse.server.repository.UserRepository;
import com.goldhouse.server.repository.specification.OrderSpecifications;
import com.goldhouse.server.service.DashboardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DashboardServiceImpl implements DashboardService {


    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final UserRepository userRepository;

    public DashboardServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.userRepository = userRepository;
    }

    @Override
    public DashboardResponseDTO getDashboardStats(long userId) {
        User user = userRepository.findUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found after authentication"));

        UserDTO userDTO = UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();


        DashboardMetrics dashboardMetricsDto = DashboardMetrics.builder()
                .totalOrders(orderRepository.count())
                .pendingOrders(orderRepository.countByOrderStatus(OrderStatus.PENDING))
                .deliveredOrders(orderRepository.countByOrderStatus(OrderStatus.DELIVERED))
                .canceledOrders(orderRepository.countByOrderStatus(OrderStatus.CANCELLED))
                .build();

        return new DashboardResponseDTO(userDTO, dashboardMetricsDto);
    }

    @Override
    public Page<OrderResponseDTO> getDashboardOrders(Pageable pageable,
                                                     String customerName,
                                                     Long customerPhoneNumber,
                                                     String orderId,
                                                     OrderStatus status,
                                                     String sortBy,
                                                     String sortDir) {
        LocalDate today = LocalDate.now();
        // Define sorting direction safely
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageableWithSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        // Combine all dynamic specifications
        Specification<Order> spec = OrderSpecifications.filterOrders(orderId, customerName, customerPhoneNumber,
                status, today);
        Page<Order> orderPage = orderRepository.findAll(spec, pageableWithSort);
        return orderPage.map(orderMapper::toResponseDTO);
    }
}
