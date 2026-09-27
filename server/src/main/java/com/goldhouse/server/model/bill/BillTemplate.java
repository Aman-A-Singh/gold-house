package com.goldhouse.server.model.bill;


import com.goldhouse.server.dto.bill.BillLineDTO;
import com.goldhouse.server.dto.bill.BillMarginsDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bill_templates")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "paper_width", nullable = false)
    private Integer paperWidth; // 58 or 80

    @Column(name = "font_size", nullable = false)
    private Integer fontSize;

    @Column(name = "is_default", nullable = false)
    private Boolean isDefault = false;

    // JSONB / JSON column mapping in Hibernate 6
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "margins", columnDefinition = "json")
    private BillMarginsDTO margins;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "top_lines", columnDefinition = "json")
    private List<BillLineDTO> topLines;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "lines", columnDefinition = "json")
    private List<BillLineDTO> lines;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "bottom_lines", columnDefinition = "json")
    private List<BillLineDTO> bottomLines;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
