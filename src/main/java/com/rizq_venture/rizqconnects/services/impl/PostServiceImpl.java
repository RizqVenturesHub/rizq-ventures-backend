package com.rizq_venture.rizqconnects.services.impl;

import com.rizq_venture.rizqconnects.dto.request.PostRequest;
import com.rizq_venture.rizqconnects.dto.response.PostResponse;
import com.rizq_venture.rizqconnects.dto.response.UserResponse;
import com.rizq_venture.rizqconnects.model.Like;
import com.rizq_venture.rizqconnects.model.Post;
import com.rizq_venture.rizqconnects.model.Users;
import com.rizq_venture.rizqconnects.repository.LikeRepo;
import com.rizq_venture.rizqconnects.repository.PostRepo;
import com.rizq_venture.rizqconnects.repository.UserRepo;
import com.rizq_venture.rizqconnects.services.PostService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepo postRepo;
    private final UserRepo userRepo;
    private final LikeRepo likeRepo;

    @Override
    public PostResponse createPost(Long userId, PostRequest request) {

        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("Id Not FOUND"));

        Post post = Post.builder()
                .user(user)
                .content(request.getContent())
                .mediaUrls(request.getMediaUrls() != null ? request.getMediaUrls() : new ArrayList<>())
                .postType(request.getPostType() != null ? request.getPostType() : Post.PostType.TEXT)
                .likesCount(0)
                .commentsCount(0)
                .build();
        post = postRepo.save(post);
        log.info("Post created by user {}: {}", userId, post.getPostId());

        return buildPostResponse(post, userId);
    }


    @Transactional(readOnly = true)
    public Page<PostResponse> getFeed(Long userId, Pageable pageable) {
        // Fetch all posts in descending order of creation time
        Page<Post> posts = postRepo.findAllByOrderByCreatedAtDesc(pageable);

        // Convert each Post entity to PostResponse
        return posts.map(post -> buildPostResponse(post, userId));
    }

    @Transactional(readOnly = true)
    public PostResponse getPostById(Long postId, Long userId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return buildPostResponse(post, userId);
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> getUserPosts(Long targetUserId, Long currentUserId, Pageable pageable) {
        Page<Post> posts = postRepo.findByUserUserIdOrderByCreatedAtDesc(targetUserId, pageable);
        return posts.map(post -> buildPostResponse(post, currentUserId));
    }

    @Transactional
    public PostResponse updatePost(Long postId, Long userId, PostRequest request) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!post.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("You can only update your own posts");
        }

        post.setContent(request.getContent());
        if (request.getMediaUrls() != null) {
            post.setMediaUrls(request.getMediaUrls());
        }
        if (request.getPostType() != null) {
            post.setPostType(request.getPostType());
        }

        post = postRepo.save(post);
        return buildPostResponse(post, userId);
    }

    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!post.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("You can only delete your own posts");
        }

        postRepo.delete(post);
        log.info("Post deleted: {}", postId);
    }

    @Transactional
    public boolean toggleLike(Long postId, Long userId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<Like> existingLike = likeRepo.findByPostPostIdAndUserUserId(postId, userId);

        if (existingLike.isPresent()) {
            // Unlike
            likeRepo.delete(existingLike.get());
            post.setLikesCount(post.getLikesCount() - 1);
            postRepo.save(post);
            return false;
        } else {
            // Like
            Like like = Like.builder()
                    .post(post)
                    .user(user)
                    .build();
            likeRepo.save(like);
            post.setLikesCount(post.getLikesCount() + 1);
            postRepo.save(post);
            return true;
        }
    }

    @Transactional(readOnly = true)
    public Long getLikesCount(Long postId) {
        return likeRepo.countByPostPostId(postId);
    }

    @Transactional(readOnly = true)
    public boolean isLikedByUser(Long postId, Long userId) {
        return likeRepo.existsByPostPostIdAndUserUserId(postId, userId);
    }


    private PostResponse buildPostResponse(Post post, Long currentUserId) {
        boolean isLiked = likeRepo.existsByPostPostIdAndUserUserId(post.getPostId(), currentUserId);

        return PostResponse.builder()
                .postId(post.getPostId())
                .author(buildUserResponse(post.getUser()))
                .content(post.getContent())
                .mediaUrls(post.getMediaUrls())
                .postType(post.getPostType())
                .likesCount(post.getLikesCount())
                .commentsCount(post.getCommentsCount())
                .isLikedByCurrentUser(isLiked)
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
    private UserResponse buildUserResponse(Users user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .headline(user.getHeadline())
                .profilePictureUrl(user.getProfilePictureUrl())
                .build();
    }

}
