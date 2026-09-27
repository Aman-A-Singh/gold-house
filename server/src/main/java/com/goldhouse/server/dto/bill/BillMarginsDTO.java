package com.goldhouse.server.dto.bill;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillMarginsDTO {
    @NotNull private Double top;
    @NotNull private Double right;
    @NotNull private Double bottom;
    @NotNull private Double left;
}
