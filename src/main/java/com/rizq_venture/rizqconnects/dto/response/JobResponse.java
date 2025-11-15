package com.rizq_venture.rizqconnects.dto.response;

import com.rizq_venture.rizqconnects.model.Job;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobResponse {
    private Long jobId;
    private UserResponse postedBy;
    private String companyName;
    private String jobTitle;
    private String jobDescription;
    private String location;
    private Job.JobType jobType;
    private Job.ExperienceLevel experienceLevel;
    private List<String> skillsRequired;
    private String salaryRange;
    private LocalDate applicationDeadline;
    private Boolean isActive;
    private Long applicationsCount;
    private Boolean hasApplied;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Double skillMatchPercentage;
}