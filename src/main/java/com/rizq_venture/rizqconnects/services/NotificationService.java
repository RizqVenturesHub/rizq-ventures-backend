package com.rizq_venture.rizqconnects.services;

import com.rizq_venture.rizqconnects.dto.response.NotificationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService {

    public Page<NotificationResponse> getUserNotification(Long userId, Pageable pageable);
    public Long getUnreadCount(Long userId);
    public void markAsRead(Long notificationId,Long userId);
    public void deleteNotification(Long notificationId, Long userId);
    public void markAllAsRead(Long userId);
}
