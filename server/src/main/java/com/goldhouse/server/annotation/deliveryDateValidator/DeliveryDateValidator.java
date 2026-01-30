package com.goldhouse.server.annotation.deliveryDateValidator;

import com.goldhouse.server.dto.orderDTO.OrderRequestDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DeliveryDateValidator implements ConstraintValidator<ValidDeliveryDate, OrderRequestDTO> {

    @Override
    public boolean isValid(OrderRequestDTO dto, ConstraintValidatorContext context) {
        if (dto.getOrderDate() == null || dto.getDeliverDate() == null) {
            return true; // Let @NotNull handle null checks
        }

        return !dto.getDeliverDate().isBefore(dto.getOrderDate());
    }
}
