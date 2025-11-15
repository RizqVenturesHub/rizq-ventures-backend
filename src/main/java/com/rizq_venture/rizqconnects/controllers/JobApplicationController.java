package com.rizq_venture.rizqconnects.controllers;

import com.rizq_venture.rizqconnects.dto.request.JobApplicationRequest;
import com.rizq_venture.rizqconnects.dto.request.UpdateApplicationStatusRequest;
import com.rizq_venture.rizqconnects.dto.response.JobApplicationResponse;
import com.rizq_venture.rizqconnects.services.JobApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    @PostMapping("/{jobId}/apply")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<JobApplicationResponse> applyForJob(
            @PathVariable Long jobId,
            @Valid @RequestBody JobApplicationRequest request,
            Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        JobApplicationResponse response = jobApplicationService.applyForJob(jobId, userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-applications")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<JobApplicationResponse>> getMyApplications(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Long userId = Long.parseLong(authentication.getName());
        Pageable pageable = PageRequest.of(page, size);
        Page<JobApplicationResponse> applications = jobApplicationService.getMyApplications(userId, pageable);
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/{jobId}/applications")
    @PreAuthorize("hasAnyRole('MENTOR', 'PARTNER')")
    public ResponseEntity<Page<JobApplicationResponse>> getJobApplications(
            @PathVariable Long jobId,
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Long userId = Long.parseLong(authentication.getName());
        Pageable pageable = PageRequest.of(page, size);
        Page<JobApplicationResponse> applications = jobApplicationService.getJobApplications(jobId, userId, pageable);
        return ResponseEntity.ok(applications);
    }

    @PutMapping("/applications/{applicationId}/status")
    @PreAuthorize("hasAnyRole('MENTOR', 'PARTNER')")
    public ResponseEntity<JobApplicationResponse> updateApplicationStatus(
            @PathVariable Long applicationId,
            @Valid @RequestBody UpdateApplicationStatusRequest request,
            Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        JobApplicationResponse response = jobApplicationService.updateApplicationStatus(applicationId, userId, request.getStatus());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{jobId}/has-applied")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Boolean> hasApplied(@PathVariable Long jobId,
                                              Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        boolean hasApplied = jobApplicationService.hasUserApplied(jobId, userId);
        return ResponseEntity.ok(hasApplied);
    }

    @GetMapping("/{jobId}/applications/count")
    @PreAuthorize("hasAnyRole('MENTOR', 'PARTNER')")
    public ResponseEntity<Long> getApplicationsCount(@PathVariable Long jobId) {
        Long count = jobApplicationService.getApplicationsCount(jobId);
        return ResponseEntity.ok(count);
    }
}
