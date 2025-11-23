package com.tam.post.service;

import com.tam.post.dto.PageResponse;
import com.tam.post.dto.request.CommentRequest;
import com.tam.post.dto.response.CommentResponse;
import com.tam.post.dto.response.UserProfileResponse;
import com.tam.post.entity.Comment;
import com.tam.post.entity.Post;
import com.tam.post.repository.CommentRepository;
import com.tam.post.repository.PostRepository;
import com.tam.post.repository.httpclient.ProfileClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentService {
    CommentRepository commentRepository;
    PostRepository postRepository;
    ProfileClient profileClient;
    DateTimeFormatter dateTimeFormatter;

    @Transactional
    public CommentResponse createComment(String postId, CommentRequest request) {
        String userId = getCurrentUserId();

        Comment comment = Comment.builder()
                .postId(postId)
                .userId(userId)
                .content(request.getContent())
                .parentCommentId(request.getParentCommentId())
                .build();

        comment = commentRepository.save(comment);

        // Tăng commentCount trong post
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        post.setCommentCount(post.getCommentCount() + 1);
        postRepository.save(post);

        return toCommentResponse(comment);
    }

    @Transactional
    public CommentResponse updateComment(String commentId, CommentRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        String userId = getCurrentUserId();
        if (!comment.getUserId().equals(userId)) {
            throw new RuntimeException("You don't have permission to update this comment");
        }

        comment.setContent(request.getContent());
        comment = commentRepository.save(comment);

        return toCommentResponse(comment);
    }

    @Transactional
    public void deleteComment(String commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        String userId = getCurrentUserId();
        if (!comment.getUserId().equals(userId)) {
            throw new RuntimeException("You don't have permission to delete this comment");
        }

        // Xóa tất cả reply của comment này
        List<Comment> replies = commentRepository.findByParentCommentId(commentId);
        commentRepository.deleteAll(replies);

        // Xóa comment
        commentRepository.delete(comment);

        // Giảm commentCount trong post (bao gồm cả replies)
        Post post = postRepository.findById(comment.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));
        post.setCommentCount(Math.max(0, post.getCommentCount() - 1 - replies.size()));
        postRepository.save(post);
    }

    public PageResponse<CommentResponse> getCommentsByPostId(String postId, int page, int size) {
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = commentRepository.findByPostIdAndParentCommentIdIsNull(postId, pageable);

        var commentList = pageData.getContent().stream()
                .map(this::toCommentResponseWithReplies)
                .collect(Collectors.toList());

        return PageResponse.<CommentResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(commentList)
                .build();
    }

    private CommentResponse toCommentResponse(Comment comment) {
        CommentResponse response = CommentResponse.builder()
                .id(comment.getId())
                .postId(comment.getPostId())
                .userId(comment.getUserId())
                .content(comment.getContent())
                .parentCommentId(comment.getParentCommentId())
                .createdDate(comment.getCreatedDate())
                .build();

        // Set formatted created time
        if (comment.getCreatedDate() != null) {
            response.setCreated(dateTimeFormatter.format(comment.getCreatedDate()));
        }

        // Lấy username
        try {
            UserProfileResponse userProfile = profileClient.getProfile(comment.getUserId()).getResult();
            String displayName = (userProfile.getFirstName() != null ? userProfile.getFirstName() : "") +
                                 (userProfile.getLastName() != null ? " " + userProfile.getLastName() : "");
            response.setUsername(displayName.trim().isEmpty() ? null : displayName.trim());
        } catch (Exception e) {
            log.error("Error while getting user profile: ", e);
            response.setUsername(null);
        }

        return response;
    }

    private CommentResponse toCommentResponseWithReplies(Comment comment) {
        CommentResponse response = toCommentResponse(comment);

        // Lấy tất cả replies
        List<Comment> replies = commentRepository.findByParentCommentId(comment.getId());
        List<CommentResponse> replyResponses = replies.stream()
                .map(this::toCommentResponse)
                .collect(Collectors.toList());

        response.setReplies(replyResponses);
        return response;
    }

    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getName())) {
            return authentication.getName();
        }
        return "default-user";
    }
}

