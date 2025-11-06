package com.rizq_venture.rizqconnects.repository;

import com.rizq_venture.rizqconnects.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepo extends JpaRepository<Like,Long> {
    boolean existsByPostPostIdAndUserUserId(Long postId, Long userId);

    Optional<Like> findByPostPostIdAndUserUserId(Long postId, Long userId);

    Long countByPostPostId(Long postId);

    void deleteByPostPostId(Long postId);
}
