package com.goldhouse.server.annotation.deliveryDateValidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.util.Date;

public class GenericDateRangeValidator implements ConstraintValidator<ValidDateRange, Object> {

    private String startDateFieldName;
    private String endDateFieldName;

    @Override
    public void initialize(ValidDateRange constraintAnnotation) {
        this.startDateFieldName = constraintAnnotation.startDateField();
        this.endDateFieldName = constraintAnnotation.endDateField();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        if (object == null) {
            return true;
        }

        try {
            Object startDateObj = getFieldValue(object, startDateFieldName);
            Object endDateObj = getFieldValue(object, endDateFieldName);

            // Let @NotNull on individual fields handle null checks
            if (startDateObj == null || endDateObj == null) {
                return true;
            }

            // Handle java.time.LocalDate
            return switch (startDateObj) {
                case ChronoLocalDate start when endDateObj instanceof ChronoLocalDate end -> !end.isBefore(start);


                // Handle java.time.LocalDateTime
                case ChronoLocalDateTime<?> start when endDateObj instanceof ChronoLocalDateTime<?> end ->
                        !end.isBefore(start);


                // Handle java.util.Date
                case Date start when endDateObj instanceof Date end -> !end.before(start);
                default -> false;
            };

        } catch (Exception e) {
            return false;
        }
    }

    private Object getFieldValue(Object object, String fieldName) throws Exception {
        Class<?> clazz = object.getClass();
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(object);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass(); // Check parent class if inherited
            }
        }
        return null;
    }
}