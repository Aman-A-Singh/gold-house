package com.goldhouse.server.dto.dashboardDTO;

import com.goldhouse.server.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponseDTO {
    private UserDTO user;
    private DashboardMetrics metrics;
}
