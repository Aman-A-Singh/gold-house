package com.goldhouse.server.annotation.deliveryDateValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DeliveryDateValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDeliveryDate {
    String message() default "Delivery date must be after or equal to the order date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
