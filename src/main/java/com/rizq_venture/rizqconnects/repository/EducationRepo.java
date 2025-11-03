package com.rizq_venture.rizqconnects.repository;

import com.rizq_venture.rizqconnects.model.Education;
import com.rizq_venture.rizqconnects.model.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EducationRepo extends JpaRepository<Education,Long> {

    List<Education> findByUserUserIdOrderByStartDateDesc(Long userId);

    Optional<Education> findByEducationIdAndUserUserId(Long educationId, Long userId);

}
