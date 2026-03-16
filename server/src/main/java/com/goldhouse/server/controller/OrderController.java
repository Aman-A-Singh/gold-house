package com.goldhouse.server.controller;

import com.goldhouse.server.api.ApiResponse;
import com.goldhouse.server.dto.homeDTO.HomeResponseDTO;
import com.goldhouse.server.dto.orderDTO.OrderRequestDTO;
import com.goldhouse.server.dto.orderDTO.OrderResponseDTO;
import com.goldhouse.server.model.OrderStatus;
import com.goldhouse.server.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Validated
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponseDTO>> addOrder(@Valid @RequestBody OrderRequestDTO dto) {
        return ResponseEntity.ok(ApiResponse.success(orderService.addOrder(dto)));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<OrderResponseDTO>>> getAllOrders() {
        List<OrderResponseDTO> orderList = orderService.getAllOrders();
        return ResponseEntity.ok(ApiResponse.successWithCount(orderList,orderList.size()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponseDTO>>> getOrders(
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) @Positive(message = "Phone number must be positive") Long customerPhoneNumber,
            @RequestParam(required = false) @Positive(message = "Customer ID must be positive") Long customerId,
            @RequestParam(required = false) OrderStatus status
    ) {
        // Priority 1: Search by specific customer details
        List<OrderResponseDTO> orderList = Collections.emptyList();
        if (customerName != null) {
            orderList = orderService.getOrdersByCustomerName(customerName);
        }
        if (customerPhoneNumber != null) {
            orderList = orderService.getOrdersByCustomerPhoneNumber(customerPhoneNumber);
        }

        // Priority 2: Filter by ID and/or Status
        if (customerId != null || status != null) {
            orderList = fetchOrders(customerId, status);
        }

        // Priority 3: Return all
        return ResponseEntity.ok(ApiResponse.successWithCount(orderList,orderList.size()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable @NotBlank(message = "Order ID cannot be empty") String id) {
        orderService.removeOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> getOrderById(@PathVariable @NotBlank(message = "Order ID cannot be empty") String id) {
        return ResponseEntity.ok(ApiResponse.success(orderService.getOrder(id)));
    }

    @GetMapping("/count")
    public long getOrdersCount(
            @RequestParam(required = false) @Positive(message = "Customer ID must be positive") Long customerId,
            @RequestParam(required = false) OrderStatus status
    ) {
        if (customerId != null && status != null) {
            return orderService.getOrdersCountByCustomerIdAndStatus(customerId, status);
        } else if (status != null) {
            return orderService.getOrdersCountByStatus(status);
        } else if (customerId != null) {
            return orderService.getTotalOrdersCountByCustomerId(customerId);
        } else {
            return orderService.getTotalOrdersCount();
        }
    }


    @GetMapping("/today")
    public ResponseEntity<ApiResponse<List<OrderResponseDTO>>> getTodayOrders() {
        List<OrderResponseDTO> orderList = orderService.getOrdersByOrderDate(LocalDate.now());
        return ResponseEntity.ok(ApiResponse.successWithCount(orderList,orderList.size()));
    }

    @GetMapping("/today/pending")
    public ResponseEntity<ApiResponse<List<OrderResponseDTO>>> getTodayPendingOrders() {
        List<OrderResponseDTO> orderList = orderService.getTodaysPendingOrder(LocalDate.now());
        return ResponseEntity.ok(ApiResponse.successWithCount(orderList,orderList.size()));
    }

    private List<OrderResponseDTO> fetchOrders(Long customerId, OrderStatus status) {
        if (customerId != null && status != null) {
            return orderService.getOrdersByCustomerIdAndStatus(customerId, status);
        } else if (customerId != null) {
            return orderService.getOrdersByCustomerID(customerId);
        } else {
            return orderService.getOrdersByStatus(status);
        }
    }

    @PutMapping("/deliver")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> updateOrderDetails(@Valid @RequestBody OrderRequestDTO dto) {
        return ResponseEntity.ok(ApiResponse.success(orderService.updateOrderDetails(dto)));
    }

    @GetMapping("/delivered/range")
    public ResponseEntity<ApiResponse<List<OrderResponseDTO>>> getOrdersDeliveredBetween(
            @RequestParam @NotNull(message = "From date is required") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam @NotNull(message = "To date is required") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate
    ) {
        List<OrderResponseDTO> orderList = orderService.ordersDeliveredBetweenDate(fromDate, toDate);
        return ResponseEntity.ok(ApiResponse.successWithCount(orderList,orderList.size()));
    }

    @GetMapping("/home")
    public ResponseEntity<ApiResponse<HomeResponseDTO>> getHomeData(@RequestParam long userId) {
        return ResponseEntity.ok(ApiResponse.success(orderService.getHomeData(userId)));
    }
}
