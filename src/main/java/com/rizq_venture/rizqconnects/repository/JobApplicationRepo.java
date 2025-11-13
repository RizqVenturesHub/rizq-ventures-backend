package com.rizq_venture.rizqconnects.repository;

import com.rizq_venture.rizqconnects.model.Job;
import com.rizq_venture.rizqconnects.model.JobApplication;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobApplicationRepo extends JpaRepository<JobApplication,Long> {
    Long countByJobJobId(Long jobId);

    boolean existsByJobJobIdAndUserUserId(Long jobId, Long currentUserId);
}
