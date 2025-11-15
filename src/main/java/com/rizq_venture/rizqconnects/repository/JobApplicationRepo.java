package com.rizq_venture.rizqconnects.repository;

import com.rizq_venture.rizqconnects.model.JobApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface JobApplicationRepo extends JpaRepository<JobApplication,Long> {

    Page<JobApplication> findByUserUserIdOrderByAppliedAtDesc(Long userId, Pageable pageable);
    Page<JobApplication> findByJobJobIdOrderByAppliedAtDesc(Long jobId, Pageable pageable);
    boolean existsByJobJobIdAndUserUserId(Long jobId, Long userId);
    Long countByJobJobId(Long jobId);
    Optional<JobApplication> findByJobJobIdAndUserUserId(Long jobId, Long userId);
}