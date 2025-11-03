package com.rizq_venture.rizqconnects.services;

import com.rizq_venture.rizqconnects.dto.response.AuthResponse;
import com.rizq_venture.rizqconnects.dto.request.LoginRequest;
import com.rizq_venture.rizqconnects.dto.request.RegisterRequest;



public interface AuthService {


    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    boolean validateToken(String token);
}
