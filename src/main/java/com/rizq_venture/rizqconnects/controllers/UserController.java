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


    @PostMapping("/myprofile/experiences")
    public ResponseEntity<ExperienceResponse> addExperience(@Valid @RequestBody ExperienceRequest request,
                                                            Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        ExperienceResponse response = userService.addExperience(userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/myprofile/experiences")
    public ResponseEntity<List<ExperienceResponse>> getExperiences(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        List<ExperienceResponse> experiences = userService.getUserExperiences(userId);
        return ResponseEntity.ok(experiences);
    }

    @PutMapping("/myprofile/experiences/{experienceId}")
    public ResponseEntity<ExperienceResponse> updateExperience(@PathVariable Long experienceId,
                                                               @Valid @RequestBody ExperienceRequest request,
                                                               Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        ExperienceResponse response = userService.updateExperience(userId, experienceId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/myprofile/experiences/{experienceId}")
    public ResponseEntity<Void> deleteExperience(@PathVariable Long experienceId,
                                                 Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        userService.deleteExperience(userId, experienceId);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/myprofile/education")
    public ResponseEntity<EducationResponse> addEducation(@Valid @RequestBody EducationRequest educationRequest
                                                          ,Authentication authentication){

        Long userId = Long.parseLong(authentication.getName());
        EducationResponse response=userService.addEducation(educationRequest,userId);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/myprofile/education")
    public ResponseEntity<List<EducationResponse>> getUserEducation
            (Authentication authentication){

        Long userId=Long.parseLong(authentication.getName());
        List<EducationResponse> responses=userService.getUserEducation(userId);

        return ResponseEntity.ok(responses);
    }

    @PutMapping("/myprofile/education/{educationId}")
    public ResponseEntity<EducationResponse> updateEducation(@PathVariable Long educationId,
                                                             @Valid @RequestBody EducationRequest request,
                                                             Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        EducationResponse response = userService.updateEducation(userId, educationId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/myprofile/education/{educationId}")
    public ResponseEntity<Void> deleteEducation(@PathVariable Long educationId,
                                                Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        userService.deleteEducation(userId, educationId);
        return ResponseEntity.noContent().build();
    }

    //Skill

    @PostMapping("/myprofile/skills")
    public ResponseEntity<SkillResponse> addSkill(@Valid @RequestBody SkillRequest request,
                                                  Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        SkillResponse response = userService.addSkill(userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/myprofile/skills")
    public ResponseEntity<List<SkillResponse>> getSkills(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        List<SkillResponse> skills = userService.getUserSkills(userId);
        return ResponseEntity.ok(skills);
    }

    @GetMapping("/myprofile/skills/primary")
    public ResponseEntity<List<SkillResponse>> getPrimarySkills(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        List<SkillResponse> skills = userService.getPrimarySkills(userId);
        return ResponseEntity.ok(skills);
    }

    @GetMapping("/myprofile/skills/secondary")
    public ResponseEntity<List<SkillResponse>> getSecondarySkills(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        List<SkillResponse> skills = userService.getSecondarySkills(userId);
        return ResponseEntity.ok(skills);
    }

    @PutMapping("/myprofile/skills/{skillId}")
    public ResponseEntity<SkillResponse> updateSkill(@PathVariable Long skillId,
                                                     @Valid @RequestBody SkillRequest request,
                                                     Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        SkillResponse response = userService.updateSkill(userId, skillId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/myprofile/skills/{skillId}")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long skillId,
                                            Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        userService.deleteSkill(userId, skillId);
        return ResponseEntity.noContent().build();
    }
}
