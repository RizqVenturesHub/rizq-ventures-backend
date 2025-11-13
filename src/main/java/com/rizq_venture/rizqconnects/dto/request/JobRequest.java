package com.rizq_venture.rizqconnects.dto.request;

import com.rizq_venture.rizqconnects.model.Job;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobRequest {

    @NotBlank(message = "Company name is required")
    @Size(max = 200, message = "Company name must not exceed 200 characters")
    private String companyName;

    @NotBlank(message = "Job title is required")
    @Size(max = 200, message = "Job title must not exceed 200 characters")
    private String jobTitle;

    @NotBlank(message = "Job description is required")
    private String jobDescription;

    @Size(max = 100, message = "Location must not exceed 100 characters")
    private String location;

    private Job.JobType jobType;

    private Job.ExperienceLevel experienceLevel;

    private List<String> skillsRequired;

    @Size(max = 100, message = "Salary range must not exceed 100 characters")
    private String salaryRange;

    private LocalDate applicationDeadline;
}