package com.goldhouse.server.dto.customerDTO;

import lombok.Data;

@Data
public class CustomerResponseDTO {
    private long id;
    private String name;
    private long phoneNumber;
}
