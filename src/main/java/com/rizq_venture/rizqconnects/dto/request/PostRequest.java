package com.rizq_venture.rizqconnects.dto.request;

import com.rizq_venture.rizqconnects.model.Post;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRequest {

    private String title;

    @NotBlank(message = "Content is required")
    private String content;

    private List<String> mediaUrls;


    private Set<String> tags;

    private Post.PostType postType;
}