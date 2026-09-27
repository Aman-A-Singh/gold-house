package com.goldhouse.server.dto.bill;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BillLineDTO {
    private String id;
    private String type;          // "field" | "text" | "divider" | "spacer" | "printedAt"
    private String label;         // for type="field"
    private String field;         // for type="field": "id", "customer", "phone", etc.
    private String text;          // for type="text"
    private String align;         // "left" | "center" | "right"
    private String style;         // "dashed" | "solid" | "double"
    private Integer fontSize;
    private Boolean bold;
    private Integer valueSize;
    private Boolean valueBold;
}
