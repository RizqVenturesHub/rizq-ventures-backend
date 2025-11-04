package com.rizq_venture.rizqconnects.model;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// ==================== Updated User Model with Roles ====================
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(length = 200)
    private String headline;

    @Column(name = "profile_picture_url", length = 500)
    private String profilePictureUrl;

    @Column(length = 100)
    private String location;

    @Column(columnDefinition = "TEXT")
    private String about;

    @Column(name = "current_position", length = 100)
    private String currentPosition;

    @Column(name = "current_company", length = 100)
    private String currentCompany;

    @Column(length = 100)
    private String industry;

    @Column(name = "website_url", length = 300)
    private String websiteUrl;

    // NEW: Role field
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private Role role = Role.USER;

    // NEW: For Partners - Organization name
    @Column(name = "organization_name", length = 200)
    private String organizationName;

    // NEW: For Mentors - Specialization areas
    @ElementCollection
    @CollectionTable(name = "mentor_specializations", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "specialization")
    private Set<String> specializations = new HashSet<>();

    // NEW: For Mentors - Years of experience
    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    // NEW: Mentor verification status
    @Column(name = "is_verified_mentor")
    @Builder.Default
    private Boolean isVerifiedMentor = false;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Experience> experiences = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Education> educations = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Skill> skills = new ArrayList<>();



    // Helper methods for role checking
    public boolean isUser() {
        return this.role == Role.USER;
    }

    public boolean isMentor() {
        return this.role == Role.MENTOR;
    }

    public boolean isPartner() {
        return this.role == Role.PARTNER;
    }

    public boolean canPostJobs() {
        return this.role == Role.MENTOR || this.role == Role.PARTNER;
    }

    public boolean canSendMessageTo(Users recipient) {
        // USER can only message MENTOR
        if (this.isUser()) {
            return recipient.isMentor();
        }

        // MENTOR can message USER
        if (this.isMentor()) {
            return recipient.isUser();
        }

        // PARTNER cannot send direct messages
        return false;
    }
}