package com.rizq_venture.rizqconnects.controllers;


import com.rizq_venture.rizqconnects.dto.AuthResponse;
import com.rizq_venture.rizqconnects.dto.LoginRequest;
import com.rizq_venture.rizqconnects.dto.RegisterRequest;
import com.rizq_venture.rizqconnects.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



    @RestController
    @RequestMapping("/api/auth")
    @RequiredArgsConstructor
    @CrossOrigin(origins = "*")
    public class AuthController {

        private final AuthService authService;

        @PostMapping("/register")
        public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
            AuthResponse response = authService.register(request);
            return ResponseEntity.ok(response);
        }

        @PostMapping("/login")
        public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        }

        @GetMapping("/validate")
        public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String token) {
            boolean isValid = authService.validateToken(token.replace("Bearer ", ""));
            return ResponseEntity.ok(isValid);
        }
    }




