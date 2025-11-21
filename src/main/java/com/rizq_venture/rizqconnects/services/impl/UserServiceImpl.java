package com.rizq_venture.rizqconnects.services.impl;

import com.rizq_venture.rizqconnects.dto.response.EducationResponse;
import com.rizq_venture.rizqconnects.dto.response.ExperienceResponse;
import com.rizq_venture.rizqconnects.dto.response.SkillResponse;
import com.rizq_venture.rizqconnects.dto.response.UserResponse;
import com.rizq_venture.rizqconnects.exception.ResourceNotFoundException;
import com.rizq_venture.rizqconnects.model.Education;
import com.rizq_venture.rizqconnects.model.Experience;
import com.rizq_venture.rizqconnects.dto.request.UpdateProfileRequest;
import com.rizq_venture.rizqconnects.model.Skill;
import com.rizq_venture.rizqconnects.model.Users;
import com.rizq_venture.rizqconnects.repository.EducationRepo;
import com.rizq_venture.rizqconnects.repository.ExperienceRepo;
import com.rizq_venture.rizqconnects.repository.SkillRepo;
import com.rizq_venture.rizqconnects.repository.UserRepo;
import com.rizq_venture.rizqconnects.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final SkillRepo skillRepo;
    private final EducationRepo educationRepo;
    private final ExperienceRepo experienceRepo;
    private final UserRepo userRepo;

    @Transactional
    public UserResponse getUserProfile(Long userId, Long currentUserId) {

        Users users = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException
                        ("User Id Not FOUND"));

        UserResponse userResponse = buildUserResponse(users);
        userResponse.setExperiences(getUserExperiences(userId));
        userResponse.setSkills(getUserSkills(userId));
        userResponse.setEducations(getUserEducation(userId));

        return userResponse;
    }

    @Transactional(readOnly = true)
    public List<EducationResponse> getUserEducation(Long userId) {
        return educationRepo.findByUserUserIdOrderByStartDateDesc(userId)
                .stream()
                .map(this::buildEducationResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ExperienceResponse> getUserExperiences(Long userId) {
        return experienceRepo.findByUserUserIdOrderByStartDateDesc(userId)
                .stream()
                .map(this::buildExperienceResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SkillResponse> getUserSkills(Long userId) {
        return skillRepo.findByUserUserIdOrderBySkillTypeAscSkillNameAsc(userId)
                .stream()
                .map(this::buildSkillResponse)
                .collect(Collectors.toList());
    }


    @Transactional
    public UserResponse updateProfile(Long userId, UpdateProfileRequest request) {
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));


        if (request.getHeadline() != null) user.setHeadline(request.getHeadline());
        if (request.getAbout() != null) user.setAbout(request.getAbout());
        if (request.getLocation() != null) user.setLocation(request.getLocation());
        if (request.getCurrentPosition() != null) user.setCurrentPosition(request.getCurrentPosition());
        if (request.getCurrentCompany() != null) user.setCurrentCompany(request.getCurrentCompany());
        if (request.getIndustry() != null) user.setIndustry(request.getIndustry());
        if (request.getWebsiteUrl() != null) user.setWebsiteUrl(request.getWebsiteUrl());
        if (request.getProfilePictureUrl() != null) user.setProfilePictureUrl(request.getProfilePictureUrl());

        user = userRepo.save(user);
        log.info("Profile updated for user ID: {}", userId);

        return buildUserResponse(user);
    }

    private UserResponse buildUserResponse(Users users) {

        return UserResponse.builder()
                .userId(users.getUserId())
                .fullName(users.getFullName())
                .email(users.getEmail())
                .headline(users.getHeadline())
                .profilePictureUrl(users.getProfilePictureUrl())
                .location(users.getLocation())
                .about(users.getAbout())
                .currentPosition(users.getCurrentPosition())
                .currentCompany(users.getCurrentCompany())
                .industry(users.getIndustry())
                .websiteUrl(users.getWebsiteUrl())
                .createdAt(users.getCreatedAt())
                .yearsOfExperience(users.getYearsOfExperience())
                .build();
    }

    private EducationResponse buildEducationResponse(Education education) {

        return EducationResponse.builder()
                .educationId(education.getEducationId())
                .schoolName(education.getSchoolName())
                .degree(education.getDegree())
                .fieldOfStudy(education.getFieldOfStudy())
                .grade(education.getGrade())
                .startDate(education.getStartDate())
                .endDate(education.getEndDate())
                .build();
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

    private SkillResponse buildSkillResponse(Skill skill) {
        return SkillResponse.builder()
                .skillId(skill.getSkillId())
                .skillName(skill.getSkillName())
                .skillType(skill.getSkillType())
                .proficiencyLevel(skill.getProficiencyLevel())
                .endorsementsCount(skill.getEndorsementsCount())
                .createdAt(skill.getCreatedAt())
                .build();
    }

}