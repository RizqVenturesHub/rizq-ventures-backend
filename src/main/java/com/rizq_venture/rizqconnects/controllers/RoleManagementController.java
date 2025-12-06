package com.rizq_venture.rizqconnects.controllers;

import com.rizq_venture.rizqconnects.dto.DirectRoleAssignmentDto;
import com.rizq_venture.rizqconnects.dto.ReviewRoleRequestDto;
import com.rizq_venture.rizqconnects.dto.RoleChangeRequestDto;
import com.rizq_venture.rizqconnects.dto.response.RoleChangeRequestResponse;
import com.rizq_venture.rizqconnects.services.RoleManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin/roles")
@RequiredArgsConstructor
public class RoleManagementController {

    private final RoleManagementService roleManagementService;


    @PostMapping("/request")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<RoleChangeRequestResponse> submitRoleChangeRequest(
            @Valid @RequestBody RoleChangeRequestDto request,
            Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        RoleChangeRequestResponse response = roleManagementService.submitRoleChangeRequest(userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-requests")
    @PreAuthorize("hasAnyRole('USER', 'MENTOR', 'PARTNER')")
    public ResponseEntity<List<RoleChangeRequestResponse>> getMyRequests(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        List<RoleChangeRequestResponse> requests = roleManagementService.getMyRequests(userId);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Page<RoleChangeRequestResponse>> getPendingRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RoleChangeRequestResponse> requests = roleManagementService.getPendingRequests(pageable);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/pending/count")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Long> getPendingCount() {
        Long count = roleManagementService.getPendingRequestsCount();
        return ResponseEntity.ok(count);
    }

    @PostMapping("/{requestId}/review")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<RoleChangeRequestResponse> reviewRoleRequest(
            @PathVariable Long requestId,
            @Valid @RequestBody ReviewRoleRequestDto request,
            Authentication authentication) {
        Long adminId = Long.parseLong(authentication.getName());
        RoleChangeRequestResponse response = roleManagementService.reviewRoleRequest(requestId, adminId, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/assign")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> directlyAssignRole(
            @Valid @RequestBody DirectRoleAssignmentDto request,
            Authentication authentication) {
        Long adminId = Long.parseLong(authentication.getName());
        roleManagementService.directlyAssignRole(adminId, request.getUserId(), request.getNewRole());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/revoke/{userId}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> revokeRole(
            @PathVariable Long userId,
            Authentication authentication) {
        Long adminId = Long.parseLong(authentication.getName());
        roleManagementService.revokeRole(adminId, userId);
        return ResponseEntity.ok().build();
    }
}