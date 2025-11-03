package com.rizq_venture.rizqconnects.services.impl;

import com.rizq_venture.rizqconnects.dto.response.AuthResponse;
import com.rizq_venture.rizqconnects.dto.request.LoginRequest;
import com.rizq_venture.rizqconnects.dto.request.RegisterRequest;
import com.rizq_venture.rizqconnects.dto.response.UserResponse;
import com.rizq_venture.rizqconnects.model.Users;
import com.rizq_venture.rizqconnects.repository.UserRepo;
import com.rizq_venture.rizqconnects.security.JwtTokenProvider;
import com.rizq_venture.rizqconnects.services.AuthService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Builder
public class AuthServiceImpl implements AuthService {


    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private  final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public AuthResponse register(RegisterRequest request) {

        log.info("Regestring new email with: {}", request.getEmail());

        if(userRepo.existsByEmail(request.getEmail())){
            System.out.println("Email already exist");
        }

        //Create User
        Users users= Users.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .headline(request.getHeadline())
                .location(request.getLocation())
                .isActive(true)
                .build();

        users=userRepo.save(users);
        log.info("User registered successfully with ID: {}",users.getUserId());

        String token= jwtTokenProvider.generateToken(users.getUserId(),
                                                     users.getEmail(),
                                                     users.getFullName());

        UserResponse userResponse=buildUserResponse(users);
        
        return AuthResponse.builder()
                .token(token)
                .user(userResponse)
                .build();

          }



    @Override
    public AuthResponse login(LoginRequest request) {

        log.info("login attempt for email: {}", request.getEmail());

        Users users = userRepo.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("Credential not FOUND"));

        if (!users.getIsActive()) {
            throw new RuntimeException("Account is deactivated");
        }
        if (!passwordEncoder.matches(request.getPassword(), users.getPasswordHash())) {
            throw new RuntimeException("Invalid Credential");
        }
        log.info("User logged in Successfully: {}", users.getUserId());


        String token = jwtTokenProvider.generateToken(
                users.getUserId(),
                users.getEmail(),
                users.getFullName()
        );

        UserResponse userResponse = buildUserResponse(users);

        return AuthResponse.builder()
                .token(token)
                .user(userResponse)
                .build();

    }

    @Override
    public boolean validateToken(String token) {

        return jwtTokenProvider.validateToken(token);
    }


    private UserResponse buildUserResponse(Users users) {

        return UserResponse.builder()
                .userId(users.getUserId())
                .email(users.getEmail())
                .fullName(users.getFullName())
                .about(users.getAbout())
                .location(users.getLocation())
                .currentCompany(users.getCurrentCompany())
                .currentPosition(users.getCurrentPosition())
                .industry(users.getIndustry())
                .headline(users.getHeadline())
                .websiteUrl(users.getWebsiteUrl())
                .createdAt(users.getCreatedAt())
                .build();
    }
}


