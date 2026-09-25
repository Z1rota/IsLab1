package org.zirota.islab1.controller;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.zirota.islab1.dto.AuthResponse;
import org.zirota.islab1.dto.LoginRequest;
import org.zirota.islab1.dto.RegisterRequest;
import org.zirota.islab1.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody RegisterRequest register) {
        authService.register(register);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest login) {
        return authService.login(login);
    }
}
