package com.goldhouse.server.dto.loginDTO;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String username;
    private String password;
}
