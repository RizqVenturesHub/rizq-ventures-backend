package com.rizq_venture.rizqconnects.services.impl;

import com.rizq_venture.rizqconnects.dto.response.JobResponse;
import com.rizq_venture.rizqconnects.dto.response.UserResponse;
import com.rizq_venture.rizqconnects.model.Job;
import com.rizq_venture.rizqconnects.model.Users;
import com.rizq_venture.rizqconnects.repository.JobRepo;
import com.rizq_venture.rizqconnects.repository.UserRepo;
import com.rizq_venture.rizqconnects.services.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchServiceImpl implements SearchService{

    private final UserRepo userRepo;
    private final JobRepo jobRepo;

    @Transactional(readOnly = true)
    public Page<UserResponse> searchUsers(String query, String location, Long currentUserId, Pageable pageable) {
        Page<Users> users = userRepo.searchUsers(query, location, pageable);
        return users.map(this::buildUserResponse);
    }

    @Transactional(readOnly = true)
    public Page<JobResponse> searchJobs(String query, String location, Long currentUserId, Pageable pageable) {
        Page<Job> jobs = jobRepo.searchJobsByQuery(query, location, pageable);
        return jobs.map(job -> buildJobResponse(job, currentUserId));
    }

    private UserResponse buildUserResponse(Users user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .headline(user.getHeadline())
                .profilePictureUrl(user.getProfilePictureUrl())
                .location(user.getLocation())
                .build();
    }

    private JobResponse buildJobResponse(Job job, Long userId) {
        return JobResponse.builder()
                .jobId(job.getJobId())
                .companyName(job.getCompanyName())
                .jobTitle(job.getJobTitle())
                .location(job.getLocation())
                .jobType(job.getJobType())
                .experienceLevel(job.getExperienceLevel())
                .salaryRange(job.getSalaryRange())
                .createdAt(job.getCreatedAt())
                .build();
    }
}
