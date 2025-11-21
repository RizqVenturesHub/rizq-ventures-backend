package com.rizq_venture.rizqconnects.services.impl;

import com.rizq_venture.rizqconnects.dto.response.AuthResponse;
import com.rizq_venture.rizqconnects.dto.request.LoginRequest;
import com.rizq_venture.rizqconnects.dto.request.RegisterRequest;
import com.rizq_venture.rizqconnects.dto.response.UserResponse;
import com.rizq_venture.rizqconnects.exception.BadRequestException;
import com.rizq_venture.rizqconnects.exception.DuplicateResourceException;
import com.rizq_venture.rizqconnects.exception.InvalidCredentialsException;
import com.rizq_venture.rizqconnects.exception.UnauthorizedException;
import com.rizq_venture.rizqconnects.model.Role;
import com.rizq_venture.rizqconnects.model.Users;
import com.rizq_venture.rizqconnects.repository.UserRepo;
import com.rizq_venture.rizqconnects.security.JwtTokenProvider;
import com.rizq_venture.rizqconnects.services.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
        public AuthResponse register(RegisterRequest request) {
        log.info("Registering new user with email: {} and role: {}",
                request.getEmail(),Role.USER);

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already registered: " + request.getEmail());
        }


        Role role = Role.USER;

        validateRoleRequirements(role, request);


        Users user = Users.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .headline(request.getHeadline())
                .location(request.getLocation())
                .role(Role.USER)
                .isActive(true)
                .build();


        if (role == Role.PARTNER) {
            user.setOrganizationName(request.getOrganizationName());
        } else if (role == Role.MENTOR) {
            user.setSpecializations(request.getSpecializations() != null
                    ? request.getSpecializations() : new HashSet<>());
            user.setYearsOfExperience(request.getYearsOfExperience());
            user.setIsVerifiedMentor(false);
        }

        user = userRepository.save(user);
        log.info("User registered successfully with ID: {} and role: {}",
                user.getUserId(), user.getRole());

        String token = jwtTokenProvider.generateToken(
                user.getUserId(),
                user.getEmail(),
                user.getFullName(),
                user.getRole()
        );


        UserResponse userResponse = buildUserResponse(user);

        return AuthResponse.builder()
                .token(token)
                .user(userResponse)
                .build();
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());

        Users user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!user.getIsActive()) {
            throw new BadRequestException("Account is deactivated");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        log.info("User logged in successfully: {} with role: {}",
                user.getUserId(), user.getRole());

        String token = jwtTokenProvider.generateToken(
                user.getUserId(),
                user.getEmail(),
                user.getFullName(),
                user.getRole()
        );

        UserResponse userResponse = buildUserResponse(user);

        return AuthResponse.builder()
                .token(token)
                .user(userResponse)
                .build();
    }

    public boolean validateToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }

    private void validateRoleRequirements(Role role, RegisterRequest request) {
        if (role == Role.PARTNER) {
            if (request.getOrganizationName() == null || request.getOrganizationName().trim().isEmpty()) {
                throw new BadRequestException("Organization name is required for Partner registration");
            }
        } else if (role == Role.MENTOR) {
            if (request.getSpecializations() == null || request.getSpecializations().isEmpty()) {
                throw new BadRequestException("At least one specialization is required for Mentor registration");
            }
            if (request.getYearsOfExperience() == null || request.getYearsOfExperience() < 0) {
                throw new UnauthorizedException("Valid years of experience is required for Mentor registration");
            }
        }
    }

    private UserResponse buildUserResponse(Users user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .headline(user.getHeadline())
                .profilePictureUrl(user.getProfilePictureUrl())
                .location(user.getLocation())
                .about(user.getAbout())
                .currentPosition(user.getCurrentPosition())
                .currentCompany(user.getCurrentCompany())
                .industry(user.getIndustry())
                .websiteUrl(user.getWebsiteUrl())
                .role(user.getRole())
                .organizationName(user.getOrganizationName())
                .specializations(user.getSpecializations())
                .yearsOfExperience(user.getYearsOfExperience())
                .isVerifiedMentor(user.getIsVerifiedMentor())
                .createdAt(user.getCreatedAt())
                .build();
    }
}