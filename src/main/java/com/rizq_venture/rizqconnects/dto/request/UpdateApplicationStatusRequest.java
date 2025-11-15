package com.rizq_venture.rizqconnects.dto.request;

import com.rizq_venture.rizqconnects.model.JobApplication;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateApplicationStatusRequest {

    @NotNull(message = "Status is required")
    private JobApplication.ApplicationStatus status;
}