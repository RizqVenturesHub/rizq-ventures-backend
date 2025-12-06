package com.rizq_venture.rizqconnects.dto.response;

import com.rizq_venture.rizqconnects.model.Role;
import com.rizq_venture.rizqconnects.model.RoleChangeRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleChangeRequestResponse {
    private Long requestId;
    private UserResponse user;
    private Role requestedRole;
    private Role currentRole;
    private String reason;
    private RoleChangeRequest.RequestStatus status;
    private UserResponse reviewedBy;
    private String adminNotes;
    private LocalDateTime createdAt;
    private LocalDateTime reviewedAt;
}