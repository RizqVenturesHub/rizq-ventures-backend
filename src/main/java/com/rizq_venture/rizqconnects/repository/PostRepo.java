package com.rizq_venture.rizqconnects.repository;

import com.rizq_venture.rizqconnects.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
@Repository
public interface PostRepo extends JpaRepository<Post, Long> {

    @EntityGraph(attributePaths = {"mediaUrls", "tags", "user"})
    @Query("""
        SELECT p FROM Post p
        WHERE p.user.userId IN :userIds
        ORDER BY p.createdAt DESC
    """)
    Page<Post> findFeedPosts(@Param("userIds") List<Long> userIds, Pageable pageable);

    @EntityGraph(attributePaths = {"mediaUrls", "tags", "user"})
    Page<Post> findByUserUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    @EntityGraph(attributePaths = {"mediaUrls", "tags", "user"})
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Long countByUserUserId(Long userId);
}
