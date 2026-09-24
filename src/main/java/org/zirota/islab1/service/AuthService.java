package org.zirota.islab1.service;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zirota.islab1.dto.AuthResponse;
import org.zirota.islab1.dto.LoginRequest;
import org.zirota.islab1.dto.RegisterRequest;
import org.zirota.islab1.entity.AppUser;
import org.zirota.islab1.entity.Role;
import org.zirota.islab1.exceptions.UsernameAlreadyExists;
import org.zirota.islab1.repository.UserRepository;
import org.zirota.islab1.security.JwtService;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, JwtService jwtService,
                       PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager ) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public void register(RegisterRequest registerRequest) {
        String username = registerRequest.username().trim();
        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyExists("Пользователь с таким логином уже существует");
        }
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(registerRequest.password()));
        user.setRole(Role.USER);
        userRepository.save(user);
    }
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        AppUser user = userRepository.findByUsername(request.username()).orElseThrow();

        String token = jwtService.generateToken(user);

        return new AuthResponse(token,user.getUsername(),user.getRole());
    }
}
