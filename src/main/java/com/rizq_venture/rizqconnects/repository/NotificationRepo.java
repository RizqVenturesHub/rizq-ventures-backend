package com.rizq_venture.rizqconnects.repository;

import com.rizq_venture.rizqconnects.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepo extends JpaRepository<Notification,Long> {

    Page<Notification> findByUserUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    Long countByUserUserIdAndIsReadFalse(Long userId);

    List<Notification> findByUserUserIdAndIsReadFalse(Long userId);

     boolean existsByUserUserIdAndReferenceIdAndType(
            Long userId,
            Long referenceId,
            Notification.NotificationType type
    );

    List<Notification> findByUserUserIdAndTypeOrderByCreatedAtDesc(
            Long userId,
            Notification.NotificationType type
    );
}
