package com.rizq_venture.rizqconnects.dto.response;

import com.rizq_venture.rizqconnects.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long userId;
    private String fullName;
    private String email;
    private String headline;
    private String profilePictureUrl;
    private String location;
    private String about;
    private String currentPosition;
    private String currentCompany;
    private String industry;
    private String websiteUrl;


    private Role role;
    private String organizationName; // For Partners
    private Set<String> specializations; // For Mentors
    private Integer yearsOfExperience; // For Mentors
    private Boolean isVerifiedMentor; // For Mentors

    private List<ExperienceResponse> experiences;
    private List<EducationResponse> educations;
    private List<SkillResponse> skills;
    private LocalDateTime createdAt;
}