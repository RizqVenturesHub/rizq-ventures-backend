package com.rizq_venture.rizqconnects.services;

import com.rizq_venture.rizqconnects.dto.request.ExperienceRequest;
import com.rizq_venture.rizqconnects.dto.response.ExperienceResponse;
import java.util.List;

public interface ExperienceService {

    public ExperienceResponse addExperience(Long userId, ExperienceRequest request);
    public List<ExperienceResponse> getUserExperiences(Long userId);
    public ExperienceResponse updateExperience(Long userId, Long experienceId, ExperienceRequest request);
    public void deleteExperience(Long userId, Long experienceId);

}
