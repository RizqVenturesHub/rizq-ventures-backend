package com.rizq_venture.rizqconnects.dto;

import com.rizq_venture.rizqconnects.model.Role;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleChangeRequestDto {

    @NotNull(message = "Requested role is required")
    private Role requestedRole;

    @NotBlank(message = "Reason is required")
    @Size(min = 50, max = 1000, message = "Reason must be between 50 and 1000 characters")
    private String reason;

    // For PARTNER role
    private String organizationName;

    // For MENTOR role
    private Set<String> specializations;

    @Min(value = 1, message = "Years of experience must be at least 1")
    private Integer yearsOfExperience;
}