package com.rizq_venture.rizqconnects.services.impl;

import com.rizq_venture.rizqconnects.dto.request.ExperienceRequest;
import com.rizq_venture.rizqconnects.dto.response.ExperienceResponse;
import com.rizq_venture.rizqconnects.exception.ResourceNotFoundException;
import com.rizq_venture.rizqconnects.model.Experience;
import com.rizq_venture.rizqconnects.model.Users;
import com.rizq_venture.rizqconnects.repository.ExperienceRepo;
import com.rizq_venture.rizqconnects.repository.UserRepo;
import com.rizq_venture.rizqconnects.services.ExperienceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExperienceServiceImpl implements ExperienceService {

    private final UserRepo userRepo;
    private final ExperienceRepo experienceRepo;

    @Transactional
    public ExperienceResponse addExperience(Long userId, ExperienceRequest request) {
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Experience experience = Experience.builder()
                .user(user)
                .companyName(request.getCompanyName())
                .jobTitle(request.getJobTitle())
                .location(request.getLocation())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .description(request.getDescription())
                .isCurrent(request.getIsCurrent() != null ?
                        request.getIsCurrent() : false)
                .build();

        experience = experienceRepo.save(experience);
        log.info("Experience added for user ID: {}", userId);

        return buildExperienceResponse(experience);
    }

    @Transactional(readOnly = true)
    public List<ExperienceResponse> getUserExperiences(Long userId) {
        return experienceRepo.findByUserUserIdOrderByStartDateDesc(userId)
                .stream()
                .map(this::buildExperienceResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ExperienceResponse updateExperience(Long userId, Long experienceId,
                                               ExperienceRequest request) {
        Experience experience = experienceRepo
                .findByExperienceIdAndUserUserId(experienceId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Experience not found"));

        experience.setCompanyName(request.getCompanyName());
        experience.setJobTitle(request.getJobTitle());
        experience.setLocation(request.getLocation());
        experience.setStartDate(request.getStartDate());
        experience.setEndDate(request.getEndDate());
        experience.setDescription(request.getDescription());
        experience.setIsCurrent(request.getIsCurrent());

        experience = experienceRepo.save(experience);
        return buildExperienceResponse(experience);
    }

    @Transactional
    public void deleteExperience(Long userId, Long experienceId) {
        Experience experience = experienceRepo.findByExperienceIdAndUserUserId(experienceId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Experience not found"));
        experienceRepo.delete(experience);
        log.info("Experience deleted: {}", experienceId);
    }

    private ExperienceResponse buildExperienceResponse(Experience exp) {
        return ExperienceResponse.builder()
                .experienceId(exp.getExperienceId())
                .companyName(exp.getCompanyName())
                .jobTitle(exp.getJobTitle())
                .location(exp.getLocation())
                .startDate(exp.getStartDate())
                .endDate(exp.getEndDate())
                .description(exp.getDescription())
                .isCurrent(exp.getIsCurrent())
                .createdAt(exp.getCreatedAt())
                .build();
    }
}
