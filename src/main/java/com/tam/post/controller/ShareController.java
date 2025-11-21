package com.tam.post.controller;

import com.tam.post.dto.ApiResponse;
import com.tam.post.dto.PageResponse;
import com.tam.post.dto.response.ShareResponse;
import com.tam.post.service.ShareService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShareController {
    ShareService shareService;

    // Share một post
    @PostMapping("/{postId}/share")
    ApiResponse<Void> sharePost(@PathVariable String postId) {
        shareService.sharePost(postId);
        return ApiResponse.<Void>builder()
                .message("Post shared successfully")
                .build();
    }

    // Unshare một post
    @DeleteMapping("/{postId}/share")
    ApiResponse<Void> unsharePost(@PathVariable String postId) {
        shareService.unsharePost(postId);
        return ApiResponse.<Void>builder()
                .message("Post unshared successfully")
                .build();
    }

    // Get shares for a post
    @GetMapping("/{postId}/shares")
    ApiResponse<PageResponse<ShareResponse>> getSharesByPostId(
            @PathVariable String postId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<ShareResponse>>builder()
                .result(shareService.getSharesByPostId(postId, page, size))
                .build();
    }
}

