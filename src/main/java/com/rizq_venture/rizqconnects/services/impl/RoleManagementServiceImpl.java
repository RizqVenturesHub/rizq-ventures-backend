package com.rizq_venture.rizqconnects.services.impl;

import com.rizq_venture.rizqconnects.dto.ReviewRoleRequestDto;
import com.rizq_venture.rizqconnects.dto.RoleChangeRequestDto;
import com.rizq_venture.rizqconnects.dto.response.RoleChangeRequestResponse;
import com.rizq_venture.rizqconnects.dto.response.UserResponse;
import com.rizq_venture.rizqconnects.exception.BadRequestException;
import com.rizq_venture.rizqconnects.exception.DuplicateResourceException;
import com.rizq_venture.rizqconnects.exception.ResourceNotFoundException;
import com.rizq_venture.rizqconnects.exception.UnauthorizedException;
import com.rizq_venture.rizqconnects.model.Notification;
import com.rizq_venture.rizqconnects.model.Role;
import com.rizq_venture.rizqconnects.model.RoleChangeRequest;
import com.rizq_venture.rizqconnects.model.Users;
import com.rizq_venture.rizqconnects.repository.NotificationRepo;
import com.rizq_venture.rizqconnects.repository.RoleChangeReqRepo;
import com.rizq_venture.rizqconnects.repository.UserRepo;
import com.rizq_venture.rizqconnects.services.RoleManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleManagementServiceImpl implements RoleManagementService {

    private final RoleChangeReqRepo roleChangeReqRepo;
    private final UserRepo userRepo;
    private final NotificationRepo notificationRepo;

    @Transactional
    public RoleChangeRequestResponse submitRoleChangeRequest(Long userId, RoleChangeRequestDto requestDto){

        Users users=userRepo.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("ID NOT FOUND"));
        if(users.getRole()==requestDto.getRequestedRole()){
            throw new BadRequestException("You already have the "+requestDto.getRequestedRole()+" role");
        }
        if(roleChangeReqRepo.existsByUserUserIdAndStatus(userId, RoleChangeRequest.RequestStatus.PENDING)){
            throw new DuplicateResourceException("You already have a pending role change request");
        }
        if(requestDto.getRequestedRole()== Role.SUPER_ADMIN){
            throw new UnauthorizedException("Cannot request SUPER_ADMIN role");
        }
        if (requestDto.getRequestedRole() == Role.USER) {
            throw new BadRequestException("Cannot downgrade to USER role through request");
        }
        validateRoleRequirements(requestDto);

        RoleChangeRequest roleChangeRequest=RoleChangeRequest.builder()
                .user(users)
                .requestedRole(requestDto.getRequestedRole())
                .currentRole(users.getRole())
                .reason(requestDto.getReason())
                .status(RoleChangeRequest.RequestStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
        roleChangeRequest=roleChangeReqRepo.save(roleChangeRequest);
        return buildRoleChangeRequestResponse(roleChangeRequest);
    }

    @Transactional(readOnly = true)
    public Page<RoleChangeRequestResponse> getPendingRequests(Pageable pageable) {
        Page<RoleChangeRequest> requests = roleChangeReqRepo
                .findByStatusOrderByCreatedAtDesc(RoleChangeRequest.RequestStatus.PENDING, pageable);
        return requests.map(this::buildRoleChangeRequestResponse);
    }

    @Transactional(readOnly = true)
    public List<RoleChangeRequestResponse> getMyRequests(Long userId) {
        List<RoleChangeRequest> requests = roleChangeReqRepo
                .findByUserUserIdOrderByCreatedAtDesc(userId);
        return requests.stream()
                .map(this::buildRoleChangeRequestResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Long getPendingRequestsCount() {
        return roleChangeReqRepo.countByStatus(RoleChangeRequest.RequestStatus.PENDING);
    }

    @Transactional
    public RoleChangeRequestResponse reviewRoleRequest(Long requestId, Long adminUserId, ReviewRoleRequestDto dto) {
        Users admin = userRepo.findById(adminUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));

        if (!admin.isSuperAdmin()) {
            throw new UnauthorizedException("Only SUPER_ADMIN can review role change requests");
        }

        RoleChangeRequest request = roleChangeReqRepo.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Role change request not found"));

        if (request.getStatus() != RoleChangeRequest.RequestStatus.PENDING) {
            throw new BadRequestException("Request has already been reviewed");
        }

        request.setStatus(dto.isApproved() ?
                RoleChangeRequest.RequestStatus.APPROVED :
                RoleChangeRequest.RequestStatus.REJECTED);
        request.setReviewedBy(admin);
        request.setAdminNotes(dto.getAdminNotes());
        request.setReviewedAt(LocalDateTime.now());

        if (dto.isApproved()) {
            // Change user's role
            Users user = request.getUser();
            Role oldRole = user.getRole();

            user.setRole(request.getRequestedRole());

            // Update role-specific fields
            if (request.getRequestedRole() == Role.MENTOR) {
                user.setIsVerifiedMentor(true);
            }

            userRepo.save(user);
            log.info("Role changed: User {} promoted from {} to {}",
                    user.getUserId(), oldRole, user.getRole());

            // Create notification for user
            createRoleChangeNotification(user, oldRole, user.getRole(), true);
        } else {
            // Create rejection notification
            createRoleChangeNotification(request.getUser(),
                    request.getCurrentRole(),
                    request.getRequestedRole(),
                    false);
        }

        request = roleChangeReqRepo.save(request);
        return buildRoleChangeRequestResponse(request);
    }

    @Transactional
    public void directlyAssignRole(Long superAdminId, Long targetUserId, Role newRole) {
        Users admin = userRepo.findById(superAdminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));

        if (!admin.isSuperAdmin()) {
            throw new UnauthorizedException("Only SUPER_ADMIN can directly assign roles");
        }

        Users targetUser = userRepo.findById(targetUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Target user not found"));

        if (newRole == Role.SUPER_ADMIN) {
            throw new BadRequestException("Cannot assign SUPER_ADMIN role through this method");
        }

        Role oldRole = targetUser.getRole();
        targetUser.setRole(newRole);

        if (newRole == Role.MENTOR) {
            targetUser.setIsVerifiedMentor(true);
        }

        userRepo.save(targetUser);

        log.info("Direct role assignment: User {} changed from {} to {} by admin {}",
                targetUserId, oldRole, newRole, superAdminId);

        createRoleChangeNotification(targetUser, oldRole, newRole, true);
    }

    @Transactional
    public void revokeRole(Long superAdminId, Long targetUserId) {
        Users admin = userRepo.findById(superAdminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));

        if (!admin.isSuperAdmin()) {
            throw new UnauthorizedException("Only SUPER_ADMIN can revoke roles");
        }

        Users targetUser = userRepo.findById(targetUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Target user not found"));

        if (targetUser.getRole() == Role.SUPER_ADMIN) {
            throw new BadRequestException("Cannot revoke SUPER_ADMIN role");
        }

        if (targetUser.getRole() == Role.USER) {
            throw new BadRequestException("User already has USER role");
        }

        Role oldRole = targetUser.getRole();
        targetUser.setRole(Role.USER);
        targetUser.setIsVerifiedMentor(false);

        userRepo.save(targetUser);

        log.info("Role revoked: User {} downgraded from {} to USER by admin {}",
                targetUserId, oldRole, superAdminId);
    }

    private void createRoleChangeNotification(Users user, Role oldRole, Role newRole, boolean approved) {
        String message;
        if (approved) {
            message = String.format("Congratulations! Your role has been upgraded from %s to %s. You can now access additional features.",
                    oldRole, newRole);
        } else {
            message = String.format("Your request to change role to %s has been reviewed and declined. Please contact support for more information.",
                    newRole);
        }

        Notification notification = Notification.builder()
                .user(user)
                .type(Notification.NotificationType.ROLE_CHANGE)
                .message(message)
                .isRead(false)
                .build();

        notificationRepo.save(notification);
    }


    private RoleChangeRequestResponse buildRoleChangeRequestResponse(RoleChangeRequest request) {
        return RoleChangeRequestResponse.builder()
                .requestId(request.getRequestId())
                .user(buildBasicUserResponse(request.getUser()))
                .requestedRole(request.getRequestedRole())
                .currentRole(request.getCurrentRole())
                .reason(request.getReason())
                .status(request.getStatus())
                .reviewedBy(request.getReviewedBy() != null ?
                        buildBasicUserResponse(request.getReviewedBy()) : null)
                .adminNotes(request.getAdminNotes())
                .createdAt(request.getCreatedAt())
                .reviewedAt(request.getReviewedAt())
                .build();
    }
    private UserResponse buildBasicUserResponse(Users user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .headline(user.getHeadline())
                .role(user.getRole())
                .build();
    }

    private void validateRoleRequirements(RoleChangeRequestDto roleChangeRequestDto){
        if(roleChangeRequestDto.getRequestedRole()==Role.PARTNER){
            if(roleChangeRequestDto.getSpecializations()==null ||  roleChangeRequestDto.getOrganizationName().trim().isEmpty()){
                throw new BadRequestException("Organization name is required for PARTNER role");
            }
        }
    }
}
