package com.rizq_venture.rizqconnects.services;

import com.rizq_venture.rizqconnects.dto.request.JobApplicationRequest;
import com.rizq_venture.rizqconnects.dto.response.JobApplicationResponse;
import com.rizq_venture.rizqconnects.model.JobApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobApplicationService {

    public JobApplicationResponse applyForJob(Long jobId, Long userId, JobApplicationRequest request);
    public Page<JobApplicationResponse> getMyApplications(Long userId, Pageable pageable);
    public JobApplicationResponse updateApplicationStatus(Long applicationId, Long userId,
                                                          JobApplication.ApplicationStatus status);
    public Long getApplicationsCount(Long jobId);
    public boolean hasUserApplied(Long jobId, Long userId);
    public Page<JobApplicationResponse> getJobApplications(Long jobId, Long userId, Pageable pageable);

}
