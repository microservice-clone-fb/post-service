package com.tam.post.controller;

import com.tam.post.dto.ApiResponse;
import com.tam.post.dto.PageResponse;
import com.tam.post.dto.response.LikeResponse;
import com.tam.post.service.LikeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LikeController {
    LikeService likeService;

    // Like một post
    @PostMapping("/{postId}/like")
    ApiResponse<Void> likePost(@PathVariable String postId) {
        likeService.likePost(postId);
        return ApiResponse.<Void>builder()
                .message("Post liked successfully")
                .build();
    }

    // Unlike một post
    @DeleteMapping("/{postId}/like")
    ApiResponse<Void> unlikePost(@PathVariable String postId) {
        likeService.unlikePost(postId);
        return ApiResponse.<Void>builder()
                .message("Post unliked successfully")
                .build();
    }

    // Get likes for a post
    @GetMapping("/{postId}/likes")
    ApiResponse<PageResponse<LikeResponse>> getLikesByPostId(
            @PathVariable String postId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<LikeResponse>>builder()
                .result(likeService.getLikesByPostId(postId, page, size))
                .build();
    }
}

