package com.rizq_venture.rizqconnects.services.impl;

import com.rizq_venture.rizqconnects.dto.LoginReq;
import com.rizq_venture.rizqconnects.dto.UserRequest;
import com.rizq_venture.rizqconnects.dto.UserResponse;
import com.rizq_venture.rizqconnects.model.Users;
import com.rizq_venture.rizqconnects.repository.UserRepo;
import com.rizq_venture.rizqconnects.security.JwtUtil;
import com.rizq_venture.rizqconnects.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ModelMapper modelMapper;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public UserResponse createUser(UserRequest userRequest) {

        Users users=modelMapper.map(userRequest,Users.class);

        users.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        Users savedUser = userRepo.save(users);

    return modelMapper.map(savedUser,UserResponse.class);
    }

    @Override
    public UserResponse updateUser(Long userId,UserRequest userRequest) {

        Users user=userRepo.findById(userId).orElseThrow
                (()->new RuntimeException("Id Not FOUnD"));
        user.setEmail(userRequest.getEmail());
        user.setAbout(userRequest.getAbout());
        user.setHeadline(user.getHeadline());
        user.setIndustry(userRequest.getIndustry());
        user.setCurrentCompany(userRequest.getCurrentCompany());
        user.setLocation(userRequest.getLocation());
        user.setFullName(user.getFullName());
        user.setWebsiteUrl(userRequest.getWebsiteUrl());
        user.setProfilePictureUrl(user.getProfilePictureUrl());
        user.setPassword(userRequest.getPassword());
        user.setCurrentPosition(userRequest.getCurrentPosition());

        Users savedUser=userRepo.save(user);
        return modelMapper.map(savedUser,UserResponse.class);
    }

    @Override
    public String userLogin(LoginReq loginReq) {

        // Find user by email (not username)
        Users user = userRepo.findByEmail(loginReq.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // Validate password using BCrypt or any encoder
        if (!passwordEncoder.matches(loginReq.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        // Generate JWT token using user email
        return jwtUtil.generateToken(user.getEmail());
    }

}
