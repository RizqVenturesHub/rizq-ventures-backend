package com.rizq_venture.rizqconnects.services;


import com.rizq_venture.rizqconnects.dto.request.EducationRequest;
import com.rizq_venture.rizqconnects.dto.request.ExperienceRequest;
import com.rizq_venture.rizqconnects.dto.request.SkillRequest;
import com.rizq_venture.rizqconnects.dto.response.EducationResponse;
import com.rizq_venture.rizqconnects.dto.response.ExperienceResponse;
import com.rizq_venture.rizqconnects.dto.response.SkillResponse;
import com.rizq_venture.rizqconnects.dto.response.UserResponse;
import com.rizq_venture.rizqconnects.dto.request.UpdateProfileRequest;

import java.util.List;

public interface UserService {

    public UserResponse getUserProfile(Long userId, Long currentUserId);
    public UserResponse updateProfile(Long userId, UpdateProfileRequest request);

}
