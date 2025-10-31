package com.rizq_venture.rizqconnects.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long userId;
    private String fullName;
    private String email;
    private String headline;
    private String profilePictureUrl;
    private String location;
    private String about;
    private String currentPosition;
    private String currentCompany;
    private String industry;
    private String websiteUrl;
    private String connectionStatus; // NOT_CONNECTED, PENDING, CONNECTED
    private Long connectionsCount;
    private LocalDateTime createdAt;

}
