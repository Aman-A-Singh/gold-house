package com.goldhouse.server.controller;

import com.goldhouse.server.api.ApiResponse;
import com.goldhouse.server.dto.dashboardDTO.DashboardResponseDTO;
import com.goldhouse.server.dto.orderDTO.OrderResponseDTO;
import com.goldhouse.server.model.OrderStatus;
import com.goldhouse.server.service.DashboardService;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private DashboardService dashboardService;

    @Autowired
    public void setDashboardService(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<DashboardResponseDTO>> getDashboardData(@RequestParam long userId) {
        return ResponseEntity.ok(ApiResponse.success(dashboardService.getDashboardStats(userId)));
    }

    // API 2: Dashboard Table Orders (Pending Orders + Today's Orders)
    @GetMapping("/orders")
    public  ResponseEntity<ApiResponse<ApiResponse.PagedResponse<OrderResponseDTO>>> getDashboardOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1") int size,
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) @Positive(message = "Phone number must be positive") Long customerPhoneNumber,
            @RequestParam(required = false) String orderId,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(defaultValue = "orderDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderResponseDTO> orderPage = dashboardService.getDashboardOrders(pageable, customerName, customerPhoneNumber, orderId, status,sortBy,sortDir);
        return ResponseEntity.ok(ApiResponse.successPaging(orderPage));
    }
}
