package com.rizq_venture.rizqconnects.dto;

import com.rizq_venture.rizqconnects.model.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DirectRoleAssignmentDto {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "New role is required")
    private Role newRole;
}