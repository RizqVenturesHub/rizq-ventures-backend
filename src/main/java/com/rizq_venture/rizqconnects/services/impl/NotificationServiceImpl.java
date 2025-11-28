package com.rizq_venture.rizqconnects.services.impl;

import com.rizq_venture.rizqconnects.dto.response.NotificationResponse;
import com.rizq_venture.rizqconnects.dto.response.UserResponse;
import com.rizq_venture.rizqconnects.exception.ResourceNotFoundException;
import com.rizq_venture.rizqconnects.exception.UnauthorizedException;
import com.rizq_venture.rizqconnects.model.Notification;
import com.rizq_venture.rizqconnects.model.Users;
import com.rizq_venture.rizqconnects.repository.NotificationRepo;
import com.rizq_venture.rizqconnects.repository.UserRepo;
import com.rizq_venture.rizqconnects.services.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepo notificationRepo;
    private final UserRepo userRepo;

    @Transactional
    public Page<NotificationResponse> getUserNotification(Long userId, Pageable pageable){

        Page<Notification> notifications=notificationRepo
                .findByUserUserIdOrderByCreatedAtDesc(userId, pageable);
        return notifications.map(this::buildNotificationResponse);
    }

    @Transactional(readOnly = true)
    public Long getUnreadCount(Long userId){
        return notificationRepo.countByUserUserIdAndIsReadFalse(userId);
    }

    @Transactional(readOnly = true)
    public void markAsRead(Long notificationId,Long userId){

        Notification notification=notificationRepo.findById(notificationId)
                .orElseThrow(()->new ResourceNotFoundException("Notification not FOUND"));

        if(!notification.getUser().getUserId().equals(userId)){
            throw new UnauthorizedException("You can only mark your own notifications as read");
        }
        notification.setIsRead(true);
        notificationRepo.save(notification);
    }

    @Transactional
    public void markAllAsRead(Long userId) {
        List<Notification> notifications = notificationRepo.findByUserUserIdAndIsReadFalse(userId);
        notifications.forEach(n -> n.setIsRead(true));
        notificationRepo.saveAll(notifications);
        log.info("All notifications marked as read for user: {}", userId);
    }
    @Transactional
    public void deleteNotification(Long notificationId, Long userId) {
        Notification notification = notificationRepo.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));

        if (!notification.getUser().getUserId().equals(userId)) {
            throw new UnauthorizedException("You can only delete your own notifications");
        }

        notificationRepo.delete(notification);
    }
    private NotificationResponse buildNotificationResponse(Notification notification) {
        return NotificationResponse.builder()
                .notificationId(notification.getNotificationId())
                .actor(notification.getActor() != null ? buildUserResponse(notification.getActor()) : null)
                .type(notification.getType())
                .message(notification.getMessage())
                .referenceId(notification.getReferenceId())
                .isRead(notification.getIsRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }

    private UserResponse buildUserResponse(Users user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .profilePictureUrl(user.getProfilePictureUrl())
                .build();
    }
}
