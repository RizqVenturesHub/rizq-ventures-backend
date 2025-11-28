package com.rizq_venture.rizqconnects.services.impl;

import com.rizq_venture.rizqconnects.dto.request.JobRequest;
import com.rizq_venture.rizqconnects.dto.response.JobResponse;
import com.rizq_venture.rizqconnects.dto.response.UserResponse;
import com.rizq_venture.rizqconnects.exception.ResourceNotFoundException;
import com.rizq_venture.rizqconnects.exception.UnauthorizedException;
import com.rizq_venture.rizqconnects.model.Job;
import com.rizq_venture.rizqconnects.model.Users;
import com.rizq_venture.rizqconnects.repository.JobApplicationRepo;
import com.rizq_venture.rizqconnects.repository.JobRepo;
import com.rizq_venture.rizqconnects.repository.SkillRepo;
import com.rizq_venture.rizqconnects.repository.UserRepo;
import com.rizq_venture.rizqconnects.services.JobMatchingService;
import com.rizq_venture.rizqconnects.services.JobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobServiceImpl implements JobService {

    private final JobRepo jobRepo;
    private final JobApplicationRepo jobApplicationRepo;
    private final UserRepo userRepo;
    private final SkillRepo skillRepo;
    private final JobMatchingService jobMatchingService;

    @Transactional
    public JobResponse createJob(Long userId, JobRequest request) {
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.canPostJobs()) {
            throw new UnauthorizedException("Only Mentors and Partners can post jobs");
        }

        Job job = Job.builder()
                .postedBy(user)
                .companyName(request.getCompanyName())
                .jobTitle(request.getJobTitle())
                .jobDescription(request.getJobDescription())
                .location(request.getLocation())
                .jobType(request.getJobType())
                .experienceLevel(request.getExperienceLevel())
                .skillsRequired(request.getSkillsRequired() != null ? request.getSkillsRequired() : new ArrayList<>())
                .salaryRange(request.getSalaryRange())
                .applicationDeadline(request.getApplicationDeadline())
                .isActive(true)
                .build();

        job = jobRepo.save(job);
        log.info("Job created by user {}: {}", userId, job.getJobId());


        try {
            jobMatchingService.notifyMatchingUsers(job);
            log.info("Skill matching completed for job {}", job.getJobId());
        } catch (Exception e) {
            log.error("Error during skill matching for job {}: {}", job.getJobId(), e.getMessage());
        }

        return buildJobResponse(job, userId);
    }

    @Transactional(readOnly = true)
    public Page<JobResponse> searchJobs(String location, String jobType, String experienceLevel,
                                        List<String> skills, String query, Long userId, Pageable pageable) {
        Page<Job> jobs;

        if (query != null && !query.isEmpty()) {
            jobs = jobRepo.searchJobsByQuery(query, location, pageable);
        } else {
            Job.JobType type = jobType != null ? Job.JobType.valueOf(jobType) : null;
            Job.ExperienceLevel level = experienceLevel != null ? Job.ExperienceLevel.valueOf(experienceLevel) : null;
            jobs = jobRepo.searchJobs(location, type, level, pageable);
        }

        return jobs.map(job -> buildJobResponse(job, userId));
    }

    @Transactional(readOnly = true)
    public JobResponse getJobById(Long jobId, Long userId) {
        Job job = jobRepo.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        JobResponse response = buildJobResponse(job, userId);
        try {
            Double matchPercentage = jobMatchingService.calculateSkillMatchPercentage(userId, jobId);
            response.setSkillMatchPercentage(matchPercentage);
        } catch (Exception e) {
            log.error("Error calculating skill match: {}", e.getMessage());
        }

        return response;
    }


    @Transactional(readOnly = true)
    public Page<JobResponse> getJobsByPoster(Long userId, Pageable pageable) {
        Page<Job> jobs = jobRepo.findByPostedByUserIdOrderByCreatedAtDesc(userId, pageable);
        return jobs.map(job -> buildJobResponse(job, userId));
    }

    @Transactional
    public JobResponse updateJob(Long jobId, Long userId, JobRequest request) {
        Job job = jobRepo.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        if (!job.getPostedBy().getUserId().equals(userId)) {
            throw new UnauthorizedException("You can only update your own job postings");
        }

        List<String> oldSkills = new ArrayList<>(job.getSkillsRequired());

        job.setCompanyName(request.getCompanyName());
        job.setJobTitle(request.getJobTitle());
        job.setJobDescription(request.getJobDescription());
        job.setLocation(request.getLocation());
        job.setJobType(request.getJobType());
        job.setExperienceLevel(request.getExperienceLevel());
        job.setSkillsRequired(request.getSkillsRequired());
        job.setSalaryRange(request.getSalaryRange());
        job.setApplicationDeadline(request.getApplicationDeadline());

        job = jobRepo.save(job);
        if (!oldSkills.equals(request.getSkillsRequired())) {
            try {
                jobMatchingService.notifyMatchingUsers(job);
                log.info("Skill matching triggered after job update {}", job.getJobId());
            } catch (Exception e) {
                log.error("Error during skill matching after update: {}", e.getMessage());
            }
        }


        return buildJobResponse(job, userId);
    }

    @Transactional
    public void deleteJob(Long jobId, Long userId) {
        Job job = jobRepo.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        if (!job.getPostedBy().getUserId().equals(userId)) {
            throw new UnauthorizedException("You can only delete your own job postings");
        }

        jobRepo.delete(job);
        log.info("Job deleted: {}", jobId);
    }
    @Transactional(readOnly = true)
    public List<JobResponse> getRecommendedJobs(Long userId, int limit) {
        List<Job> recommendedJobs = jobMatchingService.getRecommendedJobs(userId, limit);

        return recommendedJobs.stream()
                .map(job -> buildJobResponse(job, userId))
                .toList();
    }

    private JobResponse buildJobResponse(Job job, Long currentUserId) {
        boolean hasApplied = jobApplicationRepo.existsByJobJobIdAndUserUserId(job.getJobId(), currentUserId);
        Long applicationsCount = jobApplicationRepo.countByJobJobId(job.getJobId());

        return JobResponse.builder()
                .jobId(job.getJobId())
                .postedBy(buildUserResponse(job.getPostedBy()))
                .companyName(job.getCompanyName())
                .jobTitle(job.getJobTitle())
                .jobDescription(job.getJobDescription())
                .location(job.getLocation())
                .jobType(job.getJobType())
                .experienceLevel(job.getExperienceLevel())
                .skillsRequired(job.getSkillsRequired())
                .salaryRange(job.getSalaryRange())
                .applicationDeadline(job.getApplicationDeadline())
                .isActive(job.getIsActive())
                .applicationsCount(applicationsCount)
                .hasApplied(hasApplied)
                .createdAt(job.getCreatedAt())
                .updatedAt(job.getUpdatedAt())
                .build();
    }

    private UserResponse buildUserResponse(Users user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .headline(user.getHeadline())
                .profilePictureUrl(user.getProfilePictureUrl())
                .role(user.getRole())
                .build();
    }
}