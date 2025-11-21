package com.rizq_venture.rizqconnects.controllers;

import com.rizq_venture.rizqconnects.dto.response.JobResponse;
import com.rizq_venture.rizqconnects.dto.response.UserResponse;
import com.rizq_venture.rizqconnects.services.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/users")
    public ResponseEntity<Page<UserResponse>> searchUsers(
            @RequestParam String query,
            @RequestParam(required = false) String location,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Authentication authentication) {
        Long currentUserId = Long.parseLong(authentication.getName());
        Pageable pageable = PageRequest.of(page, size);
        Page<UserResponse> users = searchService.searchUsers(query, location, currentUserId, pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/jobs")
    public ResponseEntity<Page<JobResponse>> searchJobs(
            @RequestParam String query,
            @RequestParam(required = false) String location,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Authentication authentication) {
        Long currentUserId = Long.parseLong(authentication.getName());
        Pageable pageable = PageRequest.of(page, size);
        Page<JobResponse> jobs = searchService.searchJobs(query, location, currentUserId, pageable);
        return ResponseEntity.ok(jobs);
    }
}