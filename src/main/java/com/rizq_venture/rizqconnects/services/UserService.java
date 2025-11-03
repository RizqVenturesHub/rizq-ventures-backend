package com.rizq_venture.rizqconnects.services;


import com.rizq_venture.rizqconnects.dto.request.EducationRequest;
import com.rizq_venture.rizqconnects.dto.request.ExperienceRequest;
import com.rizq_venture.rizqconnects.dto.response.EducationResponse;
import com.rizq_venture.rizqconnects.dto.response.ExperienceResponse;
import com.rizq_venture.rizqconnects.dto.response.UserResponse;
import com.rizq_venture.rizqconnects.dto.request.UpdateProfileRequest;

import java.util.List;

public interface UserService {

    public UserResponse getUserProfile(Long userId, Long currentUserId);
    public UserResponse updateProfile(Long userId, UpdateProfileRequest request);

    public ExperienceResponse addExperience(Long userId, ExperienceRequest request);
    public List<ExperienceResponse> getUserExperiences(Long userId);
    public ExperienceResponse updateExperience(Long userId, Long experienceId, ExperienceRequest request);
    public void deleteExperience(Long userId, Long experienceId);

    public EducationResponse addEducation( EducationRequest request,Long userId);
    public List<EducationResponse> getUserEducation(Long userId);
    public EducationResponse updateEducation(Long userId, Long educationId, EducationRequest request);
    public void deleteEducation(Long userId, Long educationId);
}
