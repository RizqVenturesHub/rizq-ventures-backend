package com.rizq_venture.rizqconnects.model;

import jakarta.persistence.*;
        import lombok.*;
        import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;


    private String email;


    private String password;


    private String fullName;

    private String headline;               // e.g., "Software Engineer at Google"
    private String profilePictureUrl;
    private String location;


    private String about;                  // bio/description

    private String currentPosition;
    private String currentCompany;
    private String industry;
    private String websiteUrl;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles=new HashSet<>();



}

