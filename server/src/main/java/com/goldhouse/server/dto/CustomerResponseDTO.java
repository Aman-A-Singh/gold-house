package com.goldhouse.server.dto;

import lombok.Data;

@Data
public class CustomerResponseDTO {
    private long id;
    private String name;
    private long phoneNumber;
}
