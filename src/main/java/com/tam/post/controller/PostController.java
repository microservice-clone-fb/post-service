package com.tam.post.controller;

import com.tam.post.dto.ApiResponse;
import com.tam.post.dto.PageResponse;
import com.tam.post.dto.request.PostRequest;
import com.tam.post.dto.response.PostResponse;
import com.tam.post.service.PostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {
    PostService postService;

    // CREATE - Tạo post mới
    @PostMapping
    ApiResponse<PostResponse> createPost(@RequestBody PostRequest request){
        return ApiResponse.<PostResponse>builder()
                .result(postService.createPost(request))
                .build();
    }

    // READ - Lấy tất cả posts (có phân trang)
    @GetMapping
    ApiResponse<PageResponse<PostResponse>> getAllPosts(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ){
        return ApiResponse.<PageResponse<PostResponse>>builder()
                .result(postService.getAllPosts(page, size))
                .build();
    }

    // READ - Lấy post theo ID
    @GetMapping("/{postId}")
    ApiResponse<PostResponse> getPost(@PathVariable String postId){
        return ApiResponse.<PostResponse>builder()
                .result(postService.getPost(postId))
                .build();
    }

    // READ - Lấy posts của user hiện tại
    @GetMapping("/my-posts")
    ApiResponse<PageResponse<PostResponse>> myPosts(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ){
        return ApiResponse.<PageResponse<PostResponse>>builder()
                .result(postService.getMyPosts(page, size))
                .build();
    }

    // READ - Lấy posts theo userId cụ thể
    @GetMapping("/user/{userId}/posts")
    ApiResponse<PageResponse<PostResponse>> getPostsByUserId(
            @PathVariable String userId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ){
        return ApiResponse.<PageResponse<PostResponse>>builder()
                .result(postService.getPostsByUserId(userId, page, size))
                .build();
    }

    // UPDATE - Cập nhật post
    @PutMapping("/{postId}")
    ApiResponse<PostResponse> updatePost(
            @PathVariable String postId,
            @RequestBody PostRequest request
    ){
        return ApiResponse.<PostResponse>builder()
                .result(postService.updatePost(postId, request))
                .build();
    }

    // DELETE - Xóa post
    @DeleteMapping("/{postId}")
    ApiResponse<Void> deletePost(@PathVariable String postId){
        postService.deletePost(postId);
        return ApiResponse.<Void>builder()
                .message("Post deleted successfully")
                .build();
    }
}