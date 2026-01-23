package com.goldhouse.server.dto.customerDTO;

import lombok.Data;

@Data
public class CustomerRequestDTO {
    private long id;
    private String name;
    private long phoneNumber;
}
