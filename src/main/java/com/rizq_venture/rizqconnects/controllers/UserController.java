package com.rizq_venture.rizqconnects.controllers;

import com.rizq_venture.rizqconnects.dto.request.EducationRequest;
import com.rizq_venture.rizqconnects.dto.request.ExperienceRequest;
import com.rizq_venture.rizqconnects.dto.request.SkillRequest;
import com.rizq_venture.rizqconnects.dto.response.EducationResponse;
import com.rizq_venture.rizqconnects.dto.response.ExperienceResponse;
import com.rizq_venture.rizqconnects.dto.response.SkillResponse;
import com.rizq_venture.rizqconnects.dto.response.UserResponse;
import com.rizq_venture.rizqconnects.dto.request.UpdateProfileRequest;
import com.rizq_venture.rizqconnects.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/myprofile")
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        UserResponse response = userService.getUserProfile(userId, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId,
                                                    Authentication authentication) {
        Long currentUserId = Long.parseLong(authentication.getName());
        UserResponse response = userService.getUserProfile(userId, currentUserId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/myprofile")
    public ResponseEntity<UserResponse> updateProfile(@Valid @RequestBody UpdateProfileRequest request,
                                                      Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        UserResponse response = userService.updateProfile(userId, request);
        return ResponseEntity.ok(response);
    }
}
