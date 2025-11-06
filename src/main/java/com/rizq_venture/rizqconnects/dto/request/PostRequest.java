package com.rizq_venture.rizqconnects.dto.request;

import com.rizq_venture.rizqconnects.model.Post;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRequest {

    @NotBlank(message = "Content is required")
    private String content;

    private List<String> mediaUrls;

    private Post.PostType postType;
}