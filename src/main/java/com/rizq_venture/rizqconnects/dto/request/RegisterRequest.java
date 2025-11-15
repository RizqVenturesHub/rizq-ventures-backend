package com.rizq_venture.rizqconnects.dto.request;

import com.rizq_venture.rizqconnects.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
public class RegisterRequest {

    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name must not exceed 100 characters")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @Size(max = 200, message = "Headline must not exceed 200 characters")
    private String headline;

    @Size(max = 100, message = "Location must not exceed 100 characters")
    private String location;



    @Size(max = 200, message = "Organization name must not exceed 200 characters")
    private String organizationName;

    private Set<String> specializations;

    @Min(value = 0, message = "Years of experience must be non-negative")
    private Integer yearsOfExperience;
}
