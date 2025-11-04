package com.rizq_venture.rizqconnects.dto.response;

import com.rizq_venture.rizqconnects.model.Skill;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillResponse {
    private Long skillId;
    private String skillName;

    // NEW: Skill Type
    private Skill.SkillType skillType;

    // NEW: Proficiency Level
    private Integer proficiencyLevel;

    private Integer endorsementsCount;
    private LocalDateTime createdAt;
}