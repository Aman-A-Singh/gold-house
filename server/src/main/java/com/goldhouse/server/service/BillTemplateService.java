package com.goldhouse.server.service;

import com.goldhouse.server.dto.bill.BillTemplateRequestDTO;
import com.goldhouse.server.dto.bill.BillTemplateResponseDTO;

import java.util.List;

public interface BillTemplateService {
    List<BillTemplateResponseDTO> getAllTemplates();
    BillTemplateResponseDTO getTemplateById(String id);
    BillTemplateResponseDTO getDefaultTemplate();
    BillTemplateResponseDTO createTemplate(BillTemplateRequestDTO dto);
    BillTemplateResponseDTO updateTemplate(String id, BillTemplateRequestDTO dto);
    BillTemplateResponseDTO setDefaultTemplate(String id);
    void deleteTemplate(String id);
}
