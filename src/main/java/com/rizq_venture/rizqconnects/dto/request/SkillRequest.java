package com.rizq_venture.rizqconnects.dto.request;

import com.rizq_venture.rizqconnects.model.Skill;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillRequest {

    @NotBlank(message = "Skill name is required")
    @Size(max = 100, message = "Skill name must not exceed 100 characters")
    private String skillName;

    // NEW: Skill Type (PRIMARY or SECONDARY)
    @NotNull(message = "Skill type is required")
    private Skill.SkillType skillType;

    // NEW: Proficiency Level (1-5)
    @Min(value = 1, message = "Proficiency level must be between 1 and 5")
    @Max(value = 5, message = "Proficiency level must be between 1 and 5")
    private Integer proficiencyLevel;
}