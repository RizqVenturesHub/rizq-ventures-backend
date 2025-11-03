package com.rizq_venture.rizqconnects.repository;

import com.rizq_venture.rizqconnects.model.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExperienceRepo extends JpaRepository<Experience,Long> {
    List<Experience> findByUserUserIdOrderByStartDateDesc(Long userId);
    Optional<Experience> findByExperienceIdAndUserUserId(Long experienceId, Long userId);
}
