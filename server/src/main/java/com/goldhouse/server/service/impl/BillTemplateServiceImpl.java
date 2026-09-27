package com.goldhouse.server.service.impl;


import com.goldhouse.server.dto.bill.*;
import com.goldhouse.server.mapper.BillTemplateMapper;
import com.goldhouse.server.model.bill.BillTemplate;
import com.goldhouse.server.exception.customException.BadRequestException;
import com.goldhouse.server.exception.customException.ResourceNotFoundException;
import com.goldhouse.server.repository.BillTemplateRepository;
import com.goldhouse.server.service.BillTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BillTemplateServiceImpl implements BillTemplateService {

    private static final int MAX_TEMPLATES = 5;
    private final BillTemplateRepository templateRepository;
    private final BillTemplateMapper  billTemplateMapper;

    @Override
    @Transactional(readOnly = true)
    public List<BillTemplateResponseDTO> getAllTemplates() {
        List<BillTemplate> templates = templateRepository.findAll();
        // If no templates exist in DB, create and return a default one
        if (templates.isEmpty()) {
            return Collections.emptyList();
        }
        return templates.stream().map(billTemplateMapper::mapToDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BillTemplateResponseDTO getTemplateById(String id) {
        BillTemplate template = templateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found with ID: " + id));
        return billTemplateMapper.mapToDTO(template);
    }

    @Override
    @Transactional(readOnly = true)
    public BillTemplateResponseDTO getDefaultTemplate() {
        return templateRepository.findByIsDefaultTrue()
                .map(billTemplateMapper::mapToDTO)
                .orElseGet(() -> {
                    List<BillTemplate> all = templateRepository.findAll();
                    if (!all.isEmpty()) {
                        return billTemplateMapper.mapToDTO(all.get(0));
                    }
                    return createInitialDefaultTemplate();
                });
    }

    @Override
    @Transactional
    public BillTemplateResponseDTO createTemplate(BillTemplateRequestDTO dto) {
        long currentCount = templateRepository.count();
        if (currentCount >= MAX_TEMPLATES) {
            throw new BadRequestException("Maximum limit of " + MAX_TEMPLATES + " templates reached. Please delete an existing template first.");
        }

        boolean shouldBeDefault = currentCount == 0 || Boolean.TRUE.equals(dto.getIsDefault());
        if (shouldBeDefault) {
            templateRepository.resetAllDefaults();
        }

        BillTemplate entity = BillTemplate.builder()
                .name(dto.getName().trim())
                .paperWidth(dto.getPaperWidth())
                .fontSize(dto.getFontSize())
                .margins(dto.getMargins())
                .topLines(dto.getTopLines())
                .lines(dto.getLines())
                .bottomLines(dto.getBottomLines())
                .isDefault(shouldBeDefault)
                .build();

        return billTemplateMapper.mapToDTO(templateRepository.save(entity));
    }

    @Override
    @Transactional
    public BillTemplateResponseDTO updateTemplate(String id, BillTemplateRequestDTO dto) {
        BillTemplate entity = templateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found with ID: " + id));

        if (Boolean.TRUE.equals(dto.getIsDefault()) && !Boolean.TRUE.equals(entity.getIsDefault())) {
            templateRepository.resetAllDefaults();
            entity.setIsDefault(true);
        }

        entity.setName(dto.getName().trim());
        entity.setPaperWidth(dto.getPaperWidth());
        entity.setFontSize(dto.getFontSize());
        entity.setMargins(dto.getMargins());
        entity.setTopLines(dto.getTopLines());
        entity.setLines(dto.getLines());
        entity.setBottomLines(dto.getBottomLines());

        return billTemplateMapper.mapToDTO(templateRepository.save(entity));
    }

    @Override
    @Transactional
    public BillTemplateResponseDTO setDefaultTemplate(String id) {
        BillTemplate entity = templateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found with ID: " + id));

        templateRepository.resetAllDefaults();
        entity.setIsDefault(true);
        return billTemplateMapper.mapToDTO(templateRepository.save(entity));
    }

    @Override
    @Transactional
    public void deleteTemplate(String id) {
        BillTemplate entity = templateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found with ID: " + id));

        boolean wasDefault = Boolean.TRUE.equals(entity.getIsDefault());
        templateRepository.delete(entity);

        // If the deleted one was default, assign the default flag to the first available template
        if (wasDefault) {
            templateRepository.findAll().stream().findFirst().ifPresent(next -> {
                next.setIsDefault(true);
                templateRepository.save(next);
            });
        }
    }

    private BillTemplateResponseDTO createInitialDefaultTemplate() {
        BillTemplate defaultTemplate = BillTemplate.builder()
                .name("Standard Bill")
                .paperWidth(80)
                .fontSize(12)
                .isDefault(true)
                .margins(new BillMarginsDTO(2.0, 0.0, 2.0, 0.0))
                .topLines(List.of(
                        BillLineDTO.builder().id(UUID.randomUUID().toString()).type("text").text("Store Name").align("center").bold(true).fontSize(17).build(),
                        BillLineDTO.builder().id(UUID.randomUUID().toString()).type("text").text("Address").align("center").build(),
                        BillLineDTO.builder().id(UUID.randomUUID().toString()).type("text").text("owner Name").align("center").build(),
                        BillLineDTO.builder().id(UUID.randomUUID().toString()).type("text").text("Ph: +91 XXX XXX XXXX").align("center").build(),
                        BillLineDTO.builder().id(UUID.randomUUID().toString()).type("divider").style("solid").build()
                ))
                .lines(List.of(
                        BillLineDTO.builder().id(UUID.randomUUID().toString()).type("field").label("Order ID").field("id").bold(true).valueBold(true).build(),
                        BillLineDTO.builder().id(UUID.randomUUID().toString()).type("field").label("Date").field("date").build(),
                        BillLineDTO.builder().id(UUID.randomUUID().toString()).type("field").label("Customer").field("customer").build()
                ))
                .bottomLines(List.of(
                        BillLineDTO.builder().id(UUID.randomUUID().toString()).type("divider").style("dashed").build(),
                        BillLineDTO.builder().id(UUID.randomUUID().toString()).type("text").text("Thank you for your business!").align("center").bold(true).build(),
                        BillLineDTO.builder().id(UUID.randomUUID().toString()).type("printedAt").align("center").fontSize(10).build()
                ))
                .build();
        return billTemplateMapper.mapToDTO(templateRepository.save(defaultTemplate));
    }
}
