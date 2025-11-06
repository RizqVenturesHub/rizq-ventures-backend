package com.rizq_venture.rizqconnects.repository;

import com.rizq_venture.rizqconnects.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface CommentRepo extends JpaRepository<Comment,Long> {

    Page<Comment> findByPostPostIdAndParentCommentIsNullOrderByCreatedAtDesc(Long postId, Pageable pageable);

    List<Comment> findByParentCommentCommentIdOrderByCreatedAtAsc(Long parentCommentId);

    Long countByPostPostId(Long postId);

    void deleteByPostPostId(Long postId);
}
