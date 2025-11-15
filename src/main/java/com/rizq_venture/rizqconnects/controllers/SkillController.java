package com.rizq_venture.rizqconnects.controllers;

import com.rizq_venture.rizqconnects.dto.request.SkillRequest;
import com.rizq_venture.rizqconnects.dto.response.SkillResponse;
import com.rizq_venture.rizqconnects.services.SkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;

    @PostMapping("/myprofile/skills")
    public ResponseEntity<SkillResponse> addSkill(@Valid @RequestBody SkillRequest request,
                                                  Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        SkillResponse response = skillService.addSkill(userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/myprofile/skills")
    public ResponseEntity<List<SkillResponse>> getSkills(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        List<SkillResponse> skills = skillService.getUserSkills(userId);
        return ResponseEntity.ok(skills);
    }

    @GetMapping("/myprofile/skills/primary")
    public ResponseEntity<List<SkillResponse>> getPrimarySkills(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        List<SkillResponse> skills = skillService.getPrimarySkills(userId);
        return ResponseEntity.ok(skills);
    }

    @GetMapping("/myprofile/skills/secondary")
    public ResponseEntity<List<SkillResponse>> getSecondarySkills(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        List<SkillResponse> skills = skillService.getSecondarySkills(userId);
        return ResponseEntity.ok(skills);
    }

    @PutMapping("/myprofile/skills/{skillId}")
    public ResponseEntity<SkillResponse> updateSkill(@PathVariable Long skillId,
                                                     @Valid @RequestBody SkillRequest request,
                                                     Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        SkillResponse response = skillService.updateSkill(userId, skillId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/myprofile/skills/{skillId}")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long skillId,
                                            Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        skillService.deleteSkill(userId, skillId);
        return ResponseEntity.noContent().build();
    }
}
