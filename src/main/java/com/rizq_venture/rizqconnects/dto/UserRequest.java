package com.rizq_venture.rizqconnects.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {


    private String email;
    private String password;
    private String fullName;

    private String headline;               //  "Software Engineer at Google"
    private String profilePictureUrl;
    private String location;
    private String about;                  // bio/description

    private String currentPosition;
    private String currentCompany;
    private String industry;
    private String websiteUrl;
}
