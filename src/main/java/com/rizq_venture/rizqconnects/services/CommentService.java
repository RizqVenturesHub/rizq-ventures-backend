package com.rizq_venture.rizqconnects.services;

import com.rizq_venture.rizqconnects.dto.request.CommentRequest;
import com.rizq_venture.rizqconnects.dto.response.CommentResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {

    Page<CommentResponse> getComments(Long postId, Pageable pageable);

    List<CommentResponse> getReplies(Long commentId);

    CommentResponse updateComment(Long commentId, Long userId, @Valid CommentRequest request);

    void deleteComment(Long commentId, Long userId);

    Long getCommentsCount(Long postId);

    CommentResponse addComment(Long postId, Long userId, @Valid CommentRequest request);
}
