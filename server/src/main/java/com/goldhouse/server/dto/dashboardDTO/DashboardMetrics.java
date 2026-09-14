package com.goldhouse.server.dto.dashboardDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardMetrics {
    private long totalOrders;
    private long pendingOrders;
    private long deliveredOrders;
    private long canceledOrders;
}