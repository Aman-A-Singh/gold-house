package com.goldhouse.server.mapper;

import com.goldhouse.server.dto.bill.BillTemplateResponseDTO;
import com.goldhouse.server.model.bill.BillTemplate;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BillTemplateMapper {
    BillTemplateResponseDTO  mapToDTO(BillTemplate entity);
    BillTemplate mapToEntity(BillTemplateResponseDTO dto);
    List<BillTemplateResponseDTO> mapToDTOs(List<BillTemplate> entities);
}
