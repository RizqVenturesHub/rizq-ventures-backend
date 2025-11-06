package com.rizq_venture.rizqconnects.services;

import com.rizq_venture.rizqconnects.dto.request.PostRequest;
import com.rizq_venture.rizqconnects.dto.response.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {

    public PostResponse createPost(Long userId, PostRequest request);
    public Page<PostResponse> getFeed(Long userId, Pageable pageable);
    public PostResponse getPostById(Long postId, Long userId);
    public Page<PostResponse> getUserPosts(Long targetUserId, Long currentUserId, Pageable pageable);
    public PostResponse updatePost(Long postId, Long userId, PostRequest request);
    public void deletePost(Long postId, Long userId);
    public boolean toggleLike(Long postId, Long userId);
    public Long getLikesCount(Long postId);
    public boolean isLikedByUser(Long postId, Long userId);
}
