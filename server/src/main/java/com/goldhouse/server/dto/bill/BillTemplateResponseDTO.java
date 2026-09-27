package com.goldhouse.server.dto.bill;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillTemplateResponseDTO {
    private String id;
    private String name;
    private Integer paperWidth;
    private Integer fontSize;
    private BillMarginsDTO margins;
    private List<BillLineDTO> topLines;
    private List<BillLineDTO> lines;
    private List<BillLineDTO> bottomLines;
    private Boolean isDefault;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
