package com.rizq_venture.rizqconnects.services.impl;

import com.rizq_venture.rizqconnects.dto.request.JobApplicationRequest;
import com.rizq_venture.rizqconnects.dto.response.JobApplicationResponse;
import com.rizq_venture.rizqconnects.dto.response.JobResponse;
import com.rizq_venture.rizqconnects.dto.response.UserResponse;
import com.rizq_venture.rizqconnects.exception.BadRequestException;
import com.rizq_venture.rizqconnects.exception.DuplicateResourceException;
import com.rizq_venture.rizqconnects.exception.ResourceNotFoundException;
import com.rizq_venture.rizqconnects.exception.UnauthorizedException;
import com.rizq_venture.rizqconnects.model.Job;
import com.rizq_venture.rizqconnects.model.JobApplication;
import com.rizq_venture.rizqconnects.model.Role;
import com.rizq_venture.rizqconnects.model.Users;
import com.rizq_venture.rizqconnects.repository.JobApplicationRepo;
import com.rizq_venture.rizqconnects.repository.JobRepo;
import com.rizq_venture.rizqconnects.repository.UserRepo;
import com.rizq_venture.rizqconnects.services.JobApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class jobApplicationServiceImpl implements JobApplicationService {

    private final JobApplicationRepo jobApplicationRepo;
    private final UserRepo userRepo;
    private final JobRepo jobRepo;

    @Transactional
    public JobApplicationResponse applyForJob(Long jobId, Long userId, JobApplicationRequest request) {
        Job job = jobRepo.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getRole() != Role.USER) {
            throw new RuntimeException("Only regular users can apply for jobs");
        }

        if (jobApplicationRepo.existsByJobJobIdAndUserUserId(jobId, userId)) {
            throw new DuplicateResourceException("You have already applied for this job");
        }

        if (!job.getIsActive()) {
            throw new BadRequestException("This job is no longer accepting applications");
        }

        JobApplication application = JobApplication.builder()
                .job(job)
                .user(user)
                .resumeUrl(request.getResumeUrl())
                .coverLetter(request.getCoverLetter())
                .status(JobApplication.ApplicationStatus.APPLIED)
                .build();

        application = jobApplicationRepo.save(application);
        log.info("Job application submitted by user {} for job {}", userId, jobId);

        return buildJobApplicationResponse(application);
    }

    @Transactional(readOnly = true)
    public Page<JobApplicationResponse> getMyApplications(Long userId, Pageable pageable) {
        Page<JobApplication> applications = jobApplicationRepo
                .findByUserUserIdOrderByAppliedAtDesc(userId, pageable);
        return applications.map(this::buildJobApplicationResponse);
    }

    @Transactional(readOnly = true)
    public Page<JobApplicationResponse> getJobApplications(Long jobId, Long userId, Pageable pageable) {
        Job job = jobRepo.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        if (!job.getPostedBy().getUserId().equals(userId)) {
            throw new UnauthorizedException("You can only view applications for your own job postings");
        }

        Page<JobApplication> applications = jobApplicationRepo
                .findByJobJobIdOrderByAppliedAtDesc(jobId, pageable);
        return applications.map(this::buildJobApplicationResponse);
    }

    @Transactional
    public JobApplicationResponse updateApplicationStatus(Long applicationId, Long userId,
                                                          JobApplication.ApplicationStatus status) {
        JobApplication application = jobApplicationRepo.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        if (!application.getJob().getPostedBy().getUserId().equals(userId)) {
            throw new UnauthorizedException("You can only update applications for your own job postings");
        }

        application.setStatus(status);
        application = jobApplicationRepo.save(application);
        log.info("Application status updated: {} to {}", applicationId, status);

        return buildJobApplicationResponse(application);
    }

    @Transactional(readOnly = true)
    public boolean hasUserApplied(Long jobId, Long userId) {
        return jobApplicationRepo.existsByJobJobIdAndUserUserId(jobId, userId);
    }

    @Transactional(readOnly = true)
    public Long getApplicationsCount(Long jobId) {
        return jobApplicationRepo.countByJobJobId(jobId);
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

    private JobApplicationResponse buildJobApplicationResponse(JobApplication application) {
        return JobApplicationResponse.builder()
                .applicationId(application.getApplicationId())
                .job(buildJobResponse(application.getJob(), application.getUser().getUserId()))
                .applicant(buildUserResponse(application.getUser()))
                .resumeUrl(application.getResumeUrl())
                .coverLetter(application.getCoverLetter())
                .status(application.getStatus())
                .appliedAt(application.getAppliedAt())
                .updatedAt(application.getUpdatedAt())
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
