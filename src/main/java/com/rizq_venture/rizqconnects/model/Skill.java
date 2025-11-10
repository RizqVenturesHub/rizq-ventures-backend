package com.rizq_venture.rizqconnects.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "skills")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skill_id")
    private Long skillId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(name = "skill_name", nullable = false, length = 100)
    private String skillName;


    @Enumerated(EnumType.STRING)
    @Column(name = "skill_type", nullable = false, length = 20)
    @Builder.Default
    private SkillType skillType = SkillType.SECONDARY;


    @Column(name = "proficiency_level")
    private Integer proficiencyLevel;

    @Column(name = "endorsements_count")
    @Builder.Default
    private Integer endorsementsCount = 0;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum SkillType {
        PRIMARY,
        SECONDARY
    }

    // Helper methods
    public boolean isPrimary() {
        return this.skillType == SkillType.PRIMARY;
    }

    public boolean isSecondary() {
        return this.skillType == SkillType.SECONDARY;
    }
}