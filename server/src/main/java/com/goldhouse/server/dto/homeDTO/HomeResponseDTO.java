package com.goldhouse.server.dto.homeDTO;


import com.goldhouse.server.dto.orderDTO.OrderResponseDTO;
import com.goldhouse.server.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HomeResponseDTO {
    private UserDTO user;
    private HomeMetrics metrics;
    private List<OrderResponseDTO> recentOrders;
}