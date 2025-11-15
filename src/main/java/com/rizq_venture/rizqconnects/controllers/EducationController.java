package com.rizq_venture.rizqconnects.controllers;

import com.rizq_venture.rizqconnects.dto.request.EducationRequest;
import com.rizq_venture.rizqconnects.dto.response.EducationResponse;
import com.rizq_venture.rizqconnects.services.EducationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class EducationController {

    private final EducationService educationService;

    @PostMapping("/myprofile/education")
    public ResponseEntity<EducationResponse> addEducation(@Valid @RequestBody EducationRequest educationRequest
            , Authentication authentication){

        Long userId = Long.parseLong(authentication.getName());
        EducationResponse response=educationService.addEducation(educationRequest,userId);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/myprofile/education")
    public ResponseEntity<List<EducationResponse>> getUserEducation
            (Authentication authentication){

        Long userId=Long.parseLong(authentication.getName());
        List<EducationResponse> responses=educationService.getUserEducation(userId);

        return ResponseEntity.ok(responses);
    }

    @PutMapping("/myprofile/education/{educationId}")
    public ResponseEntity<EducationResponse> updateEducation(@PathVariable Long educationId,
                                                             @Valid @RequestBody EducationRequest request,
                                                             Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        EducationResponse response = educationService.updateEducation(userId, educationId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/myprofile/education/{educationId}")
    public ResponseEntity<Void> deleteEducation(@PathVariable Long educationId,
                                                Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        educationService.deleteEducation(userId, educationId);
        return ResponseEntity.noContent().build();
    }
}
