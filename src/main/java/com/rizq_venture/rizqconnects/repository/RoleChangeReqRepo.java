package com.rizq_venture.rizqconnects.repository;

import com.rizq_venture.rizqconnects.model.Role;
import com.rizq_venture.rizqconnects.model.RoleChangeRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleChangeReqRepo extends JpaRepository<RoleChangeRequest,Long> {
    Page<RoleChangeRequest> findByStatusOrderByCreatedAtDesc(
            RoleChangeRequest.RequestStatus status,
            Pageable pageable);

    List<RoleChangeRequest> findByUserUserIdOrderByCreatedAtDesc(Long userId);

    Optional<RoleChangeRequest> findByUserUserIdAndStatus(
            Long userId,
            RoleChangeRequest.RequestStatus status);

    boolean existsByUserUserIdAndStatus(
            Long userId,
            RoleChangeRequest.RequestStatus status);

    @Query("SELECT COUNT(r) FROM RoleChangeRequest r WHERE r.status = :status")
    Long countByStatus(@Param("status") RoleChangeRequest.RequestStatus status);
}