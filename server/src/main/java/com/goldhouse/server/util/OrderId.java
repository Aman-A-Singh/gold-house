package com.goldhouse.server.util;

import org.hibernate.annotations.IdGeneratorType;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({ FIELD, METHOD })
@IdGeneratorType(OrderIdGenerator.class) // Links to your logic
public @interface OrderId {
    // You can even add params here if needed
}
