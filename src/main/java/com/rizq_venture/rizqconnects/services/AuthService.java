package com.rizq_venture.rizqconnects.services;

import com.rizq_venture.rizqconnects.dto.AuthResponse;
import com.rizq_venture.rizqconnects.dto.LoginRequest;
import com.rizq_venture.rizqconnects.dto.RegisterRequest;



public interface AuthService {


    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    boolean validateToken(String token);
}
