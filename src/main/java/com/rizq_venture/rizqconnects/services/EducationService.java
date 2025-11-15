package com.rizq_venture.rizqconnects.services;

import com.rizq_venture.rizqconnects.dto.request.EducationRequest;
import com.rizq_venture.rizqconnects.dto.response.EducationResponse;
import java.util.List;

public interface EducationService {

    public EducationResponse addEducation(EducationRequest request, Long userId);
    public List<EducationResponse> getUserEducation(Long userId);
    public EducationResponse updateEducation(Long userId, Long educationId, EducationRequest request);
    public void deleteEducation(Long userId, Long educationId);


}
