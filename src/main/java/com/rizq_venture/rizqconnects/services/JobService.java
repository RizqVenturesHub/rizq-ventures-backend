package com.rizq_venture.rizqconnects.services;

import com.rizq_venture.rizqconnects.dto.request.JobApplicationRequest;
import com.rizq_venture.rizqconnects.dto.request.JobRequest;
import com.rizq_venture.rizqconnects.dto.response.JobApplicationResponse;
import com.rizq_venture.rizqconnects.dto.response.JobResponse;
import com.rizq_venture.rizqconnects.model.JobApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface JobService {

    public JobResponse createJob(Long userId, JobRequest request);
    public Page<JobResponse> searchJobs(String location, String jobType, String experienceLevel,
                                        List<String> skills, String query, Long userId, Pageable pageable);
    public JobResponse getJobById(Long jobId, Long userId);
    public Page<JobResponse> getJobsByPoster(Long userId, Pageable pageable);
    public JobResponse updateJob(Long jobId, Long userId, JobRequest request);
    public void deleteJob(Long jobId, Long userId);

       }
