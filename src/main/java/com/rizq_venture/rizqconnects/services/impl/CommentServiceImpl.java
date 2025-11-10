package com.rizq_venture.rizqconnects.services.impl;

import com.rizq_venture.rizqconnects.dto.request.CommentRequest;
import com.rizq_venture.rizqconnects.dto.response.CommentResponse;
import com.rizq_venture.rizqconnects.dto.response.UserResponse;
import com.rizq_venture.rizqconnects.model.Comment;
import com.rizq_venture.rizqconnects.model.Post;
import com.rizq_venture.rizqconnects.model.Users;
import com.rizq_venture.rizqconnects.repository.CommentRepo;
import com.rizq_venture.rizqconnects.repository.PostRepo;
import com.rizq_venture.rizqconnects.repository.UserRepo;
import com.rizq_venture.rizqconnects.services.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepo commentRepo;
    private final UserRepo userRepo;
    private final PostRepo postRepo;

    @Transactional(readOnly = true)
    public Page<CommentResponse> getComments(Long postId, Pageable pageable) {
        Page<Comment> comments = commentRepo
                .findByPostPostIdAndParentCommentIsNullOrderByCreatedAtDesc(postId, pageable);
        return comments.map(this::buildCommentResponse);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getReplies(Long commentId) {
        return commentRepo.findByParentCommentCommentIdOrderByCreatedAtAsc(commentId)
                .stream()
                .map(this::buildCommentResponse)
                .collect(Collectors.toList());
    }


    @Transactional
    public CommentResponse updateComment(Long commentId, Long userId, CommentRequest request) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("You can only update your own comments");
        }

        comment.setContent(request.getContent());
        comment = commentRepo.save(comment);
        return buildCommentResponse(comment);
    }
    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("You can only delete your own comments");
        }

        Post post = comment.getPost();
        post.setCommentsCount(post.getCommentsCount() - 1);
        postRepo.save(post);

        commentRepo.delete(comment);
        log.info("Comment deleted: {}", commentId);
    }

    @Transactional(readOnly = true)
    public Long getCommentsCount(Long postId) {
        return commentRepo.countByPostPostId(postId);
    }

    @Override
    public CommentResponse addComment(Long postId, Long userId, CommentRequest request) {
        log.info("In CommentService addComment Method");

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        Users users=userRepo.findById(userId)
                .orElseThrow(()->new RuntimeException("User not FOUND"));

        Comment parentComment=null;
        if(request.getParentCommentId() != null) {
            parentComment = commentRepo.findById(request.getParentCommentId())
                    .orElseThrow(() -> new RuntimeException("Parent comment not Found "));
        }
            Comment comment=Comment.builder()
                    .post(post)
                    .user(users)
                    .content(request.getContent())
                    .parentComment(parentComment)
                    .build();

            comment=commentRepo.save(comment);


        post.setCommentsCount(post.getCommentsCount() + 1);
        postRepo.save(post);

        log.info("Comment added to post {}: {}", postId, comment.getCommentId());
        return buildCommentResponse(comment);
        
    }

    private CommentResponse buildCommentResponse(Comment comment) {
        return CommentResponse.builder()
                .commentId(comment.getCommentId())
                .postId(comment.getPost().getPostId())
                .author(buildUserResponse(comment.getUser()))
                .content(comment.getContent())
                .parentCommentId(comment.getParentComment() != null
                        ? comment.getParentComment().getCommentId() : null)
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
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
