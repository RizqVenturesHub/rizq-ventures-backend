package com.rizq_venture.rizqconnects.services;

import com.rizq_venture.rizqconnects.dto.request.SkillRequest;
import com.rizq_venture.rizqconnects.dto.response.SkillResponse;
import java.util.List;

public interface SkillService  {

    public SkillResponse addSkill(Long userId, SkillRequest request);
    public List<SkillResponse> getUserSkills(Long userId);
    public List<SkillResponse> getPrimarySkills(Long userId);
    public List<SkillResponse> getSecondarySkills(Long userId);
    public SkillResponse updateSkill(Long userId, Long skillId, SkillRequest request);
    public void deleteSkill(Long userId, Long skillId);

}
