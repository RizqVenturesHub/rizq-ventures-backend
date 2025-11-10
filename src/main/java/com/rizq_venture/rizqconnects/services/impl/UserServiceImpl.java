package com.rizq_venture.rizqconnects.services.impl;

import com.rizq_venture.rizqconnects.dto.request.EducationRequest;
import com.rizq_venture.rizqconnects.dto.request.ExperienceRequest;
import com.rizq_venture.rizqconnects.dto.request.SkillRequest;
import com.rizq_venture.rizqconnects.dto.response.EducationResponse;
import com.rizq_venture.rizqconnects.dto.response.ExperienceResponse;
import com.rizq_venture.rizqconnects.dto.response.SkillResponse;
import com.rizq_venture.rizqconnects.dto.response.UserResponse;
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
                .orElseThrow(() -> new RuntimeException
                        ("User Id Not FOUND"));

        UserResponse userResponse = buildUserResponse(users);
        userResponse.setExperiences(getUserExperiences(userId));
        userResponse.setEducations(getUserEducation(userId));

        return userResponse;
    }
    @Transactional(readOnly = true)
    public List<ExperienceResponse> getUserExperiences(Long userId) {
        return experienceRepo.findByUserUserIdOrderByStartDateDesc(userId)
                .stream()
                .map(this::buildExperienceResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponse updateProfile(Long userId, UpdateProfileRequest request) {
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));


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

    //Experience
    @Transactional
    public ExperienceResponse addExperience(Long userId, ExperienceRequest request) {
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

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

    @Transactional
    public ExperienceResponse updateExperience(Long userId, Long experienceId,
                                               ExperienceRequest request) {
        Experience experience = experienceRepo
                .findByExperienceIdAndUserUserId(experienceId, userId)
                .orElseThrow(() -> new RuntimeException("Experience not found"));

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
                .orElseThrow(() -> new RuntimeException("Experience not found"));
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
                .build();
    }
    // Education
    @Override
    public EducationResponse addEducation( EducationRequest request,Long userId) {

        Users user=userRepo.findById(userId)
                .orElseThrow(()->new RuntimeException("ID not FOUND"));

        Education education=Education.builder()
                .user(user)
                .schoolName(request.getSchoolName())
                .degree(request.getDegree())
                .fieldOfStudy(request.getFieldOfStudy())
                .grade(request.getGrade())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();
        education=educationRepo.save(education);
        return buildEducationResponse(education);
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

    @Transactional(readOnly = true)
    public List<EducationResponse> getUserEducation(Long userId) {
        return educationRepo.findByUserUserIdOrderByStartDateDesc(userId)
                .stream()
                .map(this::buildEducationResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public EducationResponse updateEducation(Long userId, Long educationId, EducationRequest request) {
        Education education = educationRepo.findByEducationIdAndUserUserId(educationId, userId)
                .orElseThrow(() -> new RuntimeException("Education not found"));

        education.setSchoolName(request.getSchoolName());
        education.setDegree(request.getDegree());
        education.setFieldOfStudy(request.getFieldOfStudy());
        education.setStartDate(request.getStartDate());
        education.setEndDate(request.getEndDate());
        education.setGrade(request.getGrade());

        education = educationRepo.save(education);
        return buildEducationResponse(education);
    }

    @Transactional
    public void deleteEducation(Long userId, Long educationId) {
        Education education = educationRepo.findByEducationIdAndUserUserId(educationId, userId)
                .orElseThrow(() -> new RuntimeException("Education not found"));
        educationRepo.delete(education);
    }
    // Skill
    @Transactional
    public SkillResponse addSkill(Long userId, SkillRequest request) {
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (skillRepo.existsByUserUserIdAndSkillNameIgnoreCase(userId, request.getSkillName())) {
            throw new RuntimeException("Skill already exists");
        }
        if (request.getSkillType() == Skill.SkillType.PRIMARY) {
            Long primaryCount = skillRepo.countPrimarySkills(userId);
            if (primaryCount >= 5) {
                throw new RuntimeException("Maximum 5 primary skills allowed. Please set existing skills as secondary or remove some.");
            }
        }

        Skill skill = Skill.builder()
                .user(user)
                .skillName(request.getSkillName())
                .skillType(request.getSkillType())
                .proficiencyLevel(request.getProficiencyLevel())
                .endorsementsCount(0)
                .build();

        skill = skillRepo.save(skill);
        log.info("Skill added for user {}: {} ({})", userId, skill.getSkillName(), skill.getSkillType());

        return buildSkillResponse(skill);
    }

    @Transactional(readOnly = true)
    public List<SkillResponse> getUserSkills(Long userId) {
        return skillRepo.findByUserUserIdOrderBySkillTypeAscSkillNameAsc(userId)
                .stream()
                .map(this::buildSkillResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SkillResponse> getPrimarySkills(Long userId) {
        return skillRepo.findPrimarySkillsByUserId(userId)
                .stream()
                .map(this::buildSkillResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SkillResponse> getSecondarySkills(Long userId) {
        return skillRepo.findSecondarySkillsByUserId(userId)
                .stream()
                .map(this::buildSkillResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public SkillResponse updateSkill(Long userId, Long skillId, SkillRequest request) {
        Skill skill = skillRepo.findBySkillIdAndUserUserId(skillId, userId)
                .orElseThrow(() -> new RuntimeException("Skill not found"));

        if (request.getSkillType() == Skill.SkillType.PRIMARY &&
                skill.getSkillType() != Skill.SkillType.PRIMARY) {
            Long primaryCount = skillRepo.countPrimarySkills(userId);
            if (primaryCount >= 5) {
                throw new RuntimeException("Maximum 5 primary skills allowed");
            }
        }

        skill.setSkillName(request.getSkillName());
        skill.setSkillType(request.getSkillType());
        skill.setProficiencyLevel(request.getProficiencyLevel());

        skill = skillRepo.save(skill);
        log.info("Skill updated for user {}: {} ({})", userId, skill.getSkillName(), skill.getSkillType());

        return buildSkillResponse(skill);
    }

    @Transactional
    public void deleteSkill(Long userId, Long skillId) {
        Skill skill = skillRepo.findBySkillIdAndUserUserId(skillId, userId)
                .orElseThrow(() -> new RuntimeException("Skill not found"));
        skillRepo.delete(skill);
        log.info("Skill deleted: {} for user {}", skillId, userId);
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