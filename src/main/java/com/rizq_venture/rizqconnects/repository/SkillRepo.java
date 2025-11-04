package com.rizq_venture.rizqconnects.repository;


import com.rizq_venture.rizqconnects.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkillRepo extends JpaRepository<Skill, Long> {

    List<Skill> findByUserUserIdOrderBySkillTypeAscSkillNameAsc(Long userId);

    Optional<Skill> findBySkillIdAndUserUserId(Long skillId, Long userId);

    boolean existsByUserUserIdAndSkillNameIgnoreCase(Long userId, String skillName);

    // NEW: Get only primary skills for a user
    @Query("SELECT s FROM Skill s WHERE s.user.userId = :userId AND s.skillType = 'PRIMARY'")
    List<Skill> findPrimarySkillsByUserId(@Param("userId") Long userId);

    // NEW: Get only secondary skills for a user
    @Query("SELECT s FROM Skill s WHERE s.user.userId = :userId AND s.skillType = 'SECONDARY'")
    List<Skill> findSecondarySkillsByUserId(@Param("userId") Long userId);

    // NEW: Find users with specific primary skills (for job matching)
    @Query("SELECT DISTINCT s.user.userId FROM Skill s WHERE " +
            "LOWER(s.skillName) IN :skillNames AND s.skillType = 'PRIMARY' AND s.user.role = 'USER'")
    List<Long> findUserIdsWithPrimarySkills(@Param("skillNames") List<String> skillNames);

    // NEW: Count primary skills for a user
    @Query("SELECT COUNT(s) FROM Skill s WHERE s.user.userId = :userId AND s.skillType = 'PRIMARY'")
    Long countPrimarySkills(@Param("userId") Long userId);
}