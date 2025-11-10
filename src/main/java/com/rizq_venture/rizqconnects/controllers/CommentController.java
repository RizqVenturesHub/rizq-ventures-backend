package com.rizq_venture.rizqconnects.controllers;

import com.rizq_venture.rizqconnects.dto.request.CommentRequest;
import com.rizq_venture.rizqconnects.dto.response.CommentResponse;
import com.rizq_venture.rizqconnects.services.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/")
    public ResponseEntity<CommentResponse> addComment(@PathVariable Long postId,
                                                      @Valid @RequestBody CommentRequest request,
                                                      Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        CommentResponse response = commentService.addComment(postId, userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    public ResponseEntity<Page<CommentResponse>> getComments(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CommentResponse> comments = commentService.getComments(postId, pageable);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{commentId}/replies")
    public ResponseEntity<List<CommentResponse>> getReplies(@PathVariable Long postId,
                                                            @PathVariable Long commentId) {
        List<CommentResponse> replies = commentService.getReplies(commentId);
        return ResponseEntity.ok(replies);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable Long postId,
                                                         @PathVariable Long commentId,
                                                         @Valid @RequestBody CommentRequest request,
                                                         Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        CommentResponse response = commentService.updateComment(commentId, userId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long postId,
                                              @PathVariable Long commentId,
                                              Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getCommentsCount(@PathVariable Long postId) {
        Long count = commentService.getCommentsCount(postId);
        return ResponseEntity.ok(count);
    }
}