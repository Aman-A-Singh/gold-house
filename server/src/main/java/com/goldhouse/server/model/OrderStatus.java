package com.goldhouse.server.model;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDING(1),
    DELIVERED(2),
    CANCELED(3);
    private final int value;


    OrderStatus(int value) {
        this.value = value;
    }
}
