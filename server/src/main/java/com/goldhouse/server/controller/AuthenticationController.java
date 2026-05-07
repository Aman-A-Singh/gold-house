package com.goldhouse.server.controller;

import com.goldhouse.server.api.ApiResponse;
import com.goldhouse.server.dto.loginDTO.AuthenticationRequest;
import com.goldhouse.server.dto.loginDTO.AuthenticationResponse;
import com.goldhouse.server.dto.user.UserDTO;
import com.goldhouse.server.model.User;
import com.goldhouse.server.security.JwtTokenProvider;
import com.goldhouse.server.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Value("${cookie.secure}")
    private Boolean cookieSecure;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public AuthenticationController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(
            @RequestBody AuthenticationRequest request,
            HttpServletResponse response) {
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

        ResponseCookie cookie = ResponseCookie.from("gh_token", token)
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite("Strict")
                .path("/")
                .maxAge(jwtExpiration / 1000)   // convert ms → seconds
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        AuthenticationResponse authResponse = AuthenticationResponse.builder()
                .user(userDTO)
                .token(token)
                .build();

        // 5. Wrap in the standard ApiResponse
        return ResponseEntity.ok(ApiResponse.success(authResponse, "Login successful"));
    }

    // Add a logout endpoint to clear the cookie
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("gh_token", "")
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite("Strict")
                .path("/")
                .maxAge(0)      // immediately expire
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok(ApiResponse.success(null, "Logged out"));
    }

    // Validate cookie session — used by the web frontend's checkAuth()
    // Returns 200 if the JWT cookie is valid, 401 if missing/expired (Spring Security handles the 401)
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDTO>> getCurrentUser(
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserDTO dto = UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
        return ResponseEntity.ok(ApiResponse.success(dto, "OK"));
    }

}
