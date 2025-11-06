package com.rizq_venture.rizqconnects.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class CommentRequest {

    @NotBlank(message = "Content is required")
    private String content;

    private Long parentCommentId;
}