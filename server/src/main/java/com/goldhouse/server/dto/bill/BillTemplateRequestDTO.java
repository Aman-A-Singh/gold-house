package com.goldhouse.server.dto.bill;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillTemplateRequestDTO {

    @NotBlank(message = "Template name cannot be empty")
    @Size(max = 100, message = "Template name cannot exceed 100 characters")
    private String name;

    @NotNull(message = "Paper width is required (58 or 80)")
    private Integer paperWidth;   // 58 or 80

    @NotNull(message = "Font size is required")
    private Integer fontSize;

    @Valid
    @NotNull(message = "Margins cannot be null")
    private BillMarginsDTO margins;

    @NotNull private List<BillLineDTO> topLines;
    @NotNull private List<BillLineDTO> lines;
    @NotNull private List<BillLineDTO> bottomLines;

    private Boolean isDefault;
}
