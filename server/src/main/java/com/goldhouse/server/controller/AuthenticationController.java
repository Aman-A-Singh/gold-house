package com.goldhouse.server.controller;

import com.goldhouse.server.api.ApiResponse;
import com.goldhouse.server.dto.loginDTO.AuthenticationRequest;
import com.goldhouse.server.dto.loginDTO.AuthenticationResponse;
import com.goldhouse.server.dto.user.UserDTO;
import com.goldhouse.server.model.User;
import com.goldhouse.server.security.JwtTokenProvider;
import com.goldhouse.server.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public AuthenticationController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(@RequestBody AuthenticationRequest request) {
        // 1. Authenticate
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // 2. Fetch user details
        User user = userService.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found after authentication"));

        // 3. Generate Token
        String token = jwtTokenProvider.generateToken(new HashMap<>(), request.getUsername());

        // 4. Build the nested DTO structure
        UserDTO userDTO = UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();

        AuthenticationResponse authResponse = AuthenticationResponse.builder()
                .user(userDTO)
                .token(token)
                .build();

        // 5. Wrap in the standard ApiResponse
        return ResponseEntity.ok(ApiResponse.success(authResponse, "Login successful"));
    }
}
