package com.rizq_venture.rizqconnects.controllers;

import com.rizq_venture.rizqconnects.dto.request.JobRequest;
import com.rizq_venture.rizqconnects.dto.response.JobResponse;
import com.rizq_venture.rizqconnects.services.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PostMapping("/")
    public ResponseEntity<JobResponse> createJob(@Valid @RequestBody JobRequest jobRequest,
                                                 Authentication authentication){

        Long userId=Long.parseLong(authentication.getName());
        JobResponse jobResponse=jobService.createJob(userId,jobRequest);
        return ResponseEntity.ok(jobResponse);
    }

    @GetMapping
    public ResponseEntity<Page<JobResponse>> searchJobs(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String jobType,
            @RequestParam(required = false) String experienceLevel,
            @RequestParam(required = false) List<String> skills,
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        Pageable pageable = PageRequest.of(page, size);
        Page<JobResponse> jobs = jobService.searchJobs(location, jobType, experienceLevel, skills, query, userId, pageable);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<JobResponse> getJobById(@PathVariable Long jobId,
                                                  Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        JobResponse response = jobService.getJobById(jobId, userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{jobId}")
    public ResponseEntity<JobResponse> updateJob(@PathVariable Long jobId,
                                                 @Valid @RequestBody JobRequest request,
                                                 Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        JobResponse response = jobService.updateJob(jobId, userId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long jobId,
                                          Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        jobService.deleteJob(jobId, userId);
        return ResponseEntity.noContent().build();
    }
}
