package com.rizq_venture.rizqconnects.dto.response;

import com.rizq_venture.rizqconnects.model.JobApplication;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobApplicationResponse {
    private Long applicationId;
    private JobResponse job;
    private UserResponse applicant;
    private String resumeUrl;
    private String coverLetter;
    private JobApplication.ApplicationStatus status;
    private LocalDateTime appliedAt;
    private LocalDateTime updatedAt;
}
