package com.rizq_venture.rizqconnects.services;

import com.rizq_venture.rizqconnects.dto.ReviewRoleRequestDto;
import com.rizq_venture.rizqconnects.dto.RoleChangeRequestDto;
import com.rizq_venture.rizqconnects.dto.response.RoleChangeRequestResponse;
import com.rizq_venture.rizqconnects.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoleManagementService {

    public RoleChangeRequestResponse submitRoleChangeRequest(Long userId,
                                                             RoleChangeRequestDto requestDto);

    public Page<RoleChangeRequestResponse> getPendingRequests(Pageable pageable);

    public List<RoleChangeRequestResponse> getMyRequests(Long userId);

    public Long getPendingRequestsCount();

    public RoleChangeRequestResponse reviewRoleRequest(Long requestId, Long adminUserId, ReviewRoleRequestDto dto);

    public void directlyAssignRole(Long superAdminId, Long targetUserId, Role newRole);

    public void revokeRole(Long superAdminId, Long targetUserId);
}
