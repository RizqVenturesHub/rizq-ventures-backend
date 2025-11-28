package com.rizq_venture.rizqconnects.services.impl;

import com.rizq_venture.rizqconnects.exception.ResourceNotFoundException;
import com.rizq_venture.rizqconnects.model.Job;
import com.rizq_venture.rizqconnects.model.Notification;
import com.rizq_venture.rizqconnects.model.Users;
import com.rizq_venture.rizqconnects.repository.JobRepo;
import com.rizq_venture.rizqconnects.repository.NotificationRepo;
import com.rizq_venture.rizqconnects.repository.SkillRepo;
import com.rizq_venture.rizqconnects.repository.UserRepo;
import com.rizq_venture.rizqconnects.services.JobMatchingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobMatchingServiceImpl implements JobMatchingService {

    private final JobRepo jobRepo;
    private final UserRepo userRepo;
    private final SkillRepo skillRepo;
    private final NotificationRepo notificationRepo;

    @Transactional
    public void notifyMatchingUsers(Job job) {
        if(job.getSkillsRequired()==null || job.getSkillsRequired().isEmpty()){
            log.info("No skills required for job {}, skipping notification", job.getJobId());
            return;
        }
    List<String> jobSkills=job.getSkillsRequired()
            .stream()
            .map(String::toLowerCase)
            .toList();
        log.info("Finding users with primary skills matching: {}", jobSkills);

        List<Long> matchingUserIds=skillRepo.findUserIdsWithPrimarySkills(jobSkills);

        if(matchingUserIds.isEmpty()){
            log.info("No users found with matching primary skills for job {}", job.getJobId());
            return;
        }
        log.info("Found {} users with matching primary skills for job {}",matchingUserIds.size(), job.getJobId());

        for(Long userId: matchingUserIds){
            createJobMatchNotification(userId, job);

        }    }

    @Transactional
    public void createJobMatchNotification(Long userId, Job job) {
        Users users=userRepo.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("USER NOT FOUND"));

        if(notificationRepo.existsByUserUserIdAndReferenceIdAndType
                (userId,job.getJobId(),Notification.NotificationType.JOB_MATCH)){
            log.debug("Notification already exists for user {} and job {}", userId, job.getJobId());
            return;
        }

        List<String>  userPrimarySkills=skillRepo.findPrimarySkillsByUserId(userId)
                .stream().map(skill->skill.getSkillName().toLowerCase())
                .toList();

        List<String> matchingSkills=job.getSkillsRequired()
                .stream()
                .filter(jobSkill->userPrimarySkills.contains(jobSkill.toLowerCase()))
                .toList();

        String skillsText = String.join(", ", matchingSkills);
        String message = String.format(
                "New job match! '%s' at %s matches your primary skills: %s",
                job.getJobTitle(),
                job.getCompanyName(),
                skillsText
        );
        Notification notification = Notification.builder()
                .user(users)
                .actor(job.getPostedBy())
                .type(Notification.NotificationType.JOB_MATCH)
                .message(message)
                .referenceId(job.getJobId())
                .isRead(false)
                .build();

        notificationRepo.save(notification);
        log.info("Created job match notification for user {} for job {}", userId, job.getJobId());
    }

    @Transactional
    public Double calculateSkillMatchPercentage(Long userId, Long jobId) {

        Job job=jobRepo.findById(jobId)
                .orElseThrow(()->new ResourceNotFoundException("Job Not FOUND"));

        if(job.getSkillsRequired().isEmpty() || job.getSkillsRequired()==null){
            return 0.0;
        }

        List<String> userPrimarySkill=skillRepo.findPrimarySkillsByUserId(userId)
                .stream().map(skill->skill.getSkillName().toLowerCase()).toList();

        List<String> jobSkill=job.getSkillsRequired().stream()
                .map(String::toLowerCase)
                .toList();

        long matchingCount=jobSkill.stream().filter(userPrimarySkill::contains).count();
        return (double) matchingCount / jobSkill.size() * 100;
    }

    @Transactional(readOnly = true)
    public List<Job> getRecommendedJobs(Long userId, int limit) {
        List<String> userPrimarySkills = skillRepo.findPrimarySkillsByUserId(userId)
                .stream()
                .map(skill -> skill.getSkillName().toLowerCase())
                .collect(Collectors.toList());

        if (userPrimarySkills.isEmpty()) {
            log.info("User {} has no primary skills, returning empty recommendations", userId);
            return List.of();
        }

        return jobRepo.findRecommendedJobs(userPrimarySkills, limit);
    }
}

