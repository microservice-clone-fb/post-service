package com.tam.post.controller;

import com.tam.post.dto.ApiResponse;
import com.tam.post.dto.PageResponse;
import com.tam.post.dto.request.CommentRequest;
import com.tam.post.dto.response.CommentResponse;
import com.tam.post.service.CommentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentController {
    CommentService commentService;

    // Tạo comment mới hoặc reply
    @PostMapping("/{postId}/comment")
    ApiResponse<CommentResponse> createComment(
            @PathVariable String postId,
            @RequestBody CommentRequest request
    ) {
        return ApiResponse.<CommentResponse>builder()
                .result(commentService.createComment(postId, request))
                .build();
    }

    // Lấy danh sách comment của một post
    @GetMapping("/{postId}/comments")
    ApiResponse<PageResponse<CommentResponse>> getCommentsByPostId(
            @PathVariable String postId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        return ApiResponse.<PageResponse<CommentResponse>>builder()
                .result(commentService.getCommentsByPostId(postId, page, size))
                .build();
    }

    // Xóa comment
    @DeleteMapping("/comment/{commentId}")
    ApiResponse<Void> deleteComment(@PathVariable String commentId) {
        commentService.deleteComment(commentId);
        return ApiResponse.<Void>builder()
                .message("Comment deleted successfully")
                .build();
    }

    // Cập nhật comment
    @PutMapping("/comment/{commentId}")
    ApiResponse<CommentResponse> updateComment(
            @PathVariable String commentId,
            @RequestBody CommentRequest request
    ) {
        return ApiResponse.<CommentResponse>builder()
                .result(commentService.updateComment(commentId, request))
                .build();
    }
}

