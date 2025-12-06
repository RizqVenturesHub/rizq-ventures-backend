package com.rizq_venture.rizqconnects.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewRoleRequestDto {

    @NotNull(message = "Approval decision is required")
    private boolean approved;

    private String adminNotes;
}