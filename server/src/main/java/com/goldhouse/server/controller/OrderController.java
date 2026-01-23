package com.goldhouse.server.controller;

import com.goldhouse.server.dto.orderDTO.OrderRequestDTO;
import com.goldhouse.server.dto.orderDTO.OrderResponseDTO;
import com.goldhouse.server.model.OrderStatus;
import com.goldhouse.server.service.OrderService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> addOrder(@RequestBody OrderRequestDTO dto) {
        return new ResponseEntity<>(orderService.addOrder(dto), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        return new ResponseEntity<>(orderService.getAllOrders(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getOrders(
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) Long customerPhoneNumber,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) OrderStatus status
    ) {
        // Priority 1: Search by specific customer details
        if (customerName != null) {
            return new ResponseEntity<>(orderService.getOrdersByCustomerName(customerName), HttpStatus.OK);
        }
        if (customerPhoneNumber != null) {
            return new ResponseEntity<>(orderService.getOrdersByCustomerPhoneNumber(customerPhoneNumber), HttpStatus.OK);
        }

        // Priority 2: Filter by ID and/or Status
        if (customerId != null || status != null) {
            return new ResponseEntity<>(fetchOrders(customerId, status), HttpStatus.OK);
        }

        // Priority 3: Return all
        return new ResponseEntity<>(orderService.getAllOrders(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable String id) {
        orderService.removeOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable String id) {
        return new ResponseEntity<>(orderService.getOrder(id), HttpStatus.OK);
    }

    @GetMapping("/count")
    public long getOrdersCount(
            @RequestParam(required = false) Long customerId,
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
    public ResponseEntity<List<OrderResponseDTO>> getTodayOrders(){
        return new ResponseEntity<>(orderService.getOrdersByOrderDate(LocalDate.now()), HttpStatus.OK);
    }

    @GetMapping("/today/pending")
    public ResponseEntity<List<OrderResponseDTO>> getTodayPendingOrders() {
        return new ResponseEntity<>(orderService.getTodaysPendingOrder(LocalDate.now()), HttpStatus.OK);
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
    public ResponseEntity<OrderResponseDTO> updateDeliverDetails(@RequestBody OrderRequestDTO dto) {
        return new ResponseEntity<> (orderService.updateDeliverDetails(dto),HttpStatus.OK);
    }

    @GetMapping("/delivered/range")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersDeliveredBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate
    ) {
        return new ResponseEntity<>(orderService.ordersDeliveredBetweenDate(fromDate, toDate), HttpStatus.OK);
    }
}
