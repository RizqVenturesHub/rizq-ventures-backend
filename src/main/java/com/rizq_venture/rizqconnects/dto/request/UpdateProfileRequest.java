package com.rizq_venture.rizqconnects.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProfileRequest {

    @Size(max = 200, message = "Headline must not exceed 200 characters")
    private String headline;

    private String about;

    @Size(max = 100, message = "Location must not exceed 100 characters")
    private String location;

    @Size(max = 100, message = "Current position must not exceed 100 characters")
    private String currentPosition;

    @Size(max = 100, message = "Current company must not exceed 100 characters")
    private String currentCompany;

    @Size(max = 100, message = "Industry must not exceed 100 characters")
    private String industry;

    @Size(max = 300, message = "Website URL must not exceed 300 characters")
    private String websiteUrl;

    private String profilePictureUrl;
}
