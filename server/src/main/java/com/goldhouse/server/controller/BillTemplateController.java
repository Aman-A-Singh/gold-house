package com.goldhouse.server.controller;

import com.goldhouse.server.api.ApiResponse;
import com.goldhouse.server.dto.bill.BillTemplateRequestDTO;
import com.goldhouse.server.dto.bill.BillTemplateResponseDTO;
import com.goldhouse.server.service.BillTemplateService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/bill-templates")
@RequiredArgsConstructor
public class BillTemplateController {

    private final BillTemplateService billTemplateService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<BillTemplateResponseDTO>>> getAllTemplates() {
        return ResponseEntity.ok(ApiResponse.success(billTemplateService.getAllTemplates()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BillTemplateResponseDTO>> getTemplateById(
            @PathVariable @NotBlank(message = "Template ID cannot be empty") String id) {
        return ResponseEntity.ok(ApiResponse.success(billTemplateService.getTemplateById(id)));
    }

    @GetMapping("/default")
    public ResponseEntity<ApiResponse<BillTemplateResponseDTO>> getDefaultTemplate() {
        return ResponseEntity.ok(ApiResponse.success(billTemplateService.getDefaultTemplate()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BillTemplateResponseDTO>> createTemplate(
            @Valid @RequestBody BillTemplateRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(billTemplateService.createTemplate(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BillTemplateResponseDTO>> updateTemplate(
            @PathVariable @NotBlank(message = "Template ID cannot be empty") String id,
            @Valid @RequestBody BillTemplateRequestDTO dto) {
        return ResponseEntity.ok(ApiResponse.success(billTemplateService.updateTemplate(id, dto)));
    }

    @PatchMapping("/{id}/default")
    public ResponseEntity<ApiResponse<BillTemplateResponseDTO>> setDefaultTemplate(
            @PathVariable @NotBlank(message = "Template ID cannot be empty") String id) {
        return ResponseEntity.ok(ApiResponse.success(billTemplateService.setDefaultTemplate(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTemplate(
            @PathVariable @NotBlank(message = "Template ID cannot be empty") String id) {
        billTemplateService.deleteTemplate(id);
        return ResponseEntity.noContent().build();
    }
}
