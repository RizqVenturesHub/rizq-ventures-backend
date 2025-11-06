package com.rizq_venture.rizqconnects.controllers;

import com.rizq_venture.rizqconnects.dto.request.PostRequest;
import com.rizq_venture.rizqconnects.dto.response.PostResponse;
import com.rizq_venture.rizqconnects.services.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody PostRequest request,
                                                   Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        PostResponse response = postService.createPost(userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/feed")
    public ResponseEntity<Page<PostResponse>> getFeed(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Long userId = Long.parseLong(authentication.getName());
        Pageable pageable = PageRequest.of(page, size);
        Page<PostResponse> feed = postService.getFeed(userId, pageable);
        return ResponseEntity.ok(feed);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long postId,
                                                    Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        PostResponse response = postService.getPostById(postId, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<PostResponse>> getUserPosts(
            @PathVariable Long userId,
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Long currentUserId = Long.parseLong(authentication.getName());
        Pageable pageable = PageRequest.of(page, size);
        Page<PostResponse> posts = postService.getUserPosts(userId, currentUserId, pageable);
        return ResponseEntity.ok(posts);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long postId,
                                                   @Valid @RequestBody PostRequest request,
                                                   Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        PostResponse response = postService.updatePost(postId, userId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId,
                                           Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        postService.deletePost(postId, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<Boolean> toggleLike(@PathVariable Long postId,
                                              Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        boolean isLiked = postService.toggleLike(postId, userId);
        return ResponseEntity.ok(isLiked);
    }

    @GetMapping("/{postId}/likes/count")
    public ResponseEntity<Long> getLikesCount(@PathVariable Long postId) {
        Long count = postService.getLikesCount(postId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/{postId}/is-liked")
    public ResponseEntity<Boolean> isLikedByUser(@PathVariable Long postId,
                                                 Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        boolean isLiked = postService.isLikedByUser(postId, userId);
        return ResponseEntity.ok(isLiked);
    }
}