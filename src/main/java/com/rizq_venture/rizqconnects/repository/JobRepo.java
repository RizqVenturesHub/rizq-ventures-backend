package com.rizq_venture.rizqconnects.repository;

import com.rizq_venture.rizqconnects.model.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepo extends JpaRepository<Job,Long> {

    @Query("SELECT j FROM Job j WHERE j.isActive = true " +
            "AND (:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))) " +
            "AND (:jobType IS NULL OR j.jobType = :jobType) " +
            "AND (:experienceLevel IS NULL OR j.experienceLevel = :experienceLevel) " +
            "ORDER BY j.createdAt DESC")
    Page<Job> searchJobs(@Param("location") String location,
                         @Param("jobType") Job.JobType jobType,
                         @Param("experienceLevel") Job.ExperienceLevel experienceLevel,
                         Pageable pageable);

    @Query("SELECT j FROM Job j WHERE j.isActive = true " +
            "AND (LOWER(j.jobTitle) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(j.companyName) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(j.jobDescription) LIKE LOWER(CONCAT('%', :query, '%'))) " +
            "AND (:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))) " +
            "ORDER BY j.createdAt DESC")
    Page<Job> searchJobsByQuery(@Param("query") String query,
                                @Param("location") String location,
                                Pageable pageable);

    Page<Job> findByPostedByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    @Query(value = "SELECT DISTINCT j.* FROM jobs j " +
            "JOIN job_skills js ON j.job_id = js.job_id " +
            "WHERE j.is_active = true " +
            "AND LOWER(js.skill_name) IN :skills " +
            "ORDER BY j.created_at DESC " +
            "LIMIT :limit", nativeQuery = true)
    List<Job> findRecommendedJobs(@Param("skills") List<String> skills, @Param("limit") int limit);

}
