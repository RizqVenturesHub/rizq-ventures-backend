package com.rizq_venture.rizqconnects.controllers;

import com.rizq_venture.rizqconnects.dto.request.ExperienceRequest;
import com.rizq_venture.rizqconnects.dto.response.ExperienceResponse;
import com.rizq_venture.rizqconnects.services.ExperienceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class ExperienceController {

    private final ExperienceService experienceService;

    @PostMapping("/myprofile/experiences")
    public ResponseEntity<ExperienceResponse> addExperience(@Valid @RequestBody ExperienceRequest request,
                                                            Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        ExperienceResponse response = experienceService.addExperience(userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/myprofile/experiences")
    public ResponseEntity<List<ExperienceResponse>> getExperiences(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        List<ExperienceResponse> experiences = experienceService.getUserExperiences(userId);
        return ResponseEntity.ok(experiences);
    }

    @PutMapping("/myprofile/experiences/{experienceId}")
    public ResponseEntity<ExperienceResponse> updateExperience(@PathVariable Long experienceId,
                                                               @Valid @RequestBody ExperienceRequest request,
                                                               Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        ExperienceResponse response = experienceService.updateExperience(userId, experienceId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/myprofile/experiences/{experienceId}")
    public ResponseEntity<Void> deleteExperience(@PathVariable Long experienceId,
                                                 Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        experienceService.deleteExperience(userId, experienceId);
        return ResponseEntity.noContent().build();
    }
}
