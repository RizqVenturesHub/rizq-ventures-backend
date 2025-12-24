package com.rizq_venture.rizqconnects.dto.response;

import com.rizq_venture.rizqconnects.model.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponse {
    private Long postId;
    private UserResponse author;
    private String title; // NEW
    private String content;
    private List<String> mediaUrls;
    private Set<String> tags;
    private Post.PostType postType;
    private Integer likesCount;
    private Integer commentsCount;
    private Boolean isLikedByCurrentUser;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}