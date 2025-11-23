package com.tam.post.service;

import com.tam.post.dto.PageResponse;
import com.tam.post.dto.request.PostRequest;
import com.tam.post.dto.response.PostResponse;
import com.tam.post.dto.response.UserProfileResponse;
import com.tam.post.entity.Post;
import com.tam.post.mapper.PostMapper;
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

import java.time.Instant;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostService {
    DateTimeFormatter dateTimeFormatter;
    PostRepository postRepository;
    PostMapper postMapper;
    ProfileClient profileClient;
    LikeService likeService;
    ShareService shareService;

    public PostResponse createPost(PostRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getName())) {
            throw new RuntimeException("User must be authenticated to create a post");
        }

        String userId = authentication.getName();

        Post post = Post.builder()
                .content(request.getContent())
                .mediaUrl(request.getMediaUrl())
                .userId(userId)
                .build();

        post = postRepository.save(post);
        PostResponse postResponse = postMapper.toPostResponse(post);

        // Set formatted created time
        if (post.getCreatedDate() != null) {
            postResponse.setCreated(dateTimeFormatter.format(post.getCreatedDate()));
        }

        // Lấy username từ profile service
        try {
            UserProfileResponse userProfile = profileClient.getProfile(userId).getResult();
            String displayName = (userProfile.getFirstName() != null ? userProfile.getFirstName() : "") +
                    (userProfile.getLastName() != null ? " " + userProfile.getLastName() : "");
            postResponse.setUsername(displayName.trim().isEmpty() ? null : displayName.trim());
        } catch (Exception e) {
            log.error("Error while getting user profile: ", e);
            postResponse.setUsername(null);
        }

        return postResponse;
    }

    public PostResponse updatePost(String postId, PostRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = "default-user";
        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getName())) {
            userId = authentication.getName();
        }

        // Kiểm tra quyền sở hữu
        if (!post.getUserId().equals(userId)) {
            throw new RuntimeException("You don't have permission to update this post");
        }

        post.setContent(request.getContent());
        post.setMediaUrl(request.getMediaUrl());
        post = postRepository.save(post);

        return postMapper.toPostResponse(post);
    }

    public void deletePost(String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = "default-user";
        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getName())) {
            userId = authentication.getName();
        }

        // Kiểm tra quyền sở hữu
        if (!post.getUserId().equals(userId)) {
            throw new RuntimeException("You don't have permission to delete this post");
        }

        postRepository.deleteById(postId);
    }

    public PageResponse<PostResponse> getAllPosts(int page, int size) {
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = postRepository.findAll(pageable);

        String currentUserId = getCurrentUserId();

        var postList = pageData.getContent().stream().map(post -> {
            var postResponse = postMapper.toPostResponse(post);

            // Set formatted created time
            if (post.getCreatedDate() != null) {
                postResponse.setCreated(dateTimeFormatter.format(post.getCreatedDate()));
            }

            // Set like/share status
            postResponse.setLiked(likeService.isPostLikedByUser(post.getId(), currentUserId));
            postResponse.setShared(shareService.isPostSharedByUser(post.getId(), currentUserId));

            // Lấy thông tin user cho mỗi post
            try {
                UserProfileResponse userProfile = profileClient.getProfile(post.getUserId()).getResult();
                String displayName = (userProfile.getFirstName() != null ? userProfile.getFirstName() : "") +
                        (userProfile.getLastName() != null ? " " + userProfile.getLastName() : "");
                postResponse.setUsername(displayName.trim().isEmpty() ? null : displayName.trim());
            } catch (Exception e) {
                log.error("error while getting user profile: ", e);
                postResponse.setUsername(null);
            }

            return postResponse;
        }).toList();

        return PageResponse.<PostResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(postList)
                .build();
    }

    public PostResponse getPost(String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        PostResponse postResponse = postMapper.toPostResponse(post);

        // Set formatted created time
        if (post.getCreatedDate() != null) {
            postResponse.setCreated(dateTimeFormatter.format(post.getCreatedDate()));
        }

        // Set like/share status
        String currentUserId = getCurrentUserId();
        postResponse.setLiked(likeService.isPostLikedByUser(post.getId(), currentUserId));
        postResponse.setShared(shareService.isPostSharedByUser(post.getId(), currentUserId));

        // Lấy thông tin user
        try {
            UserProfileResponse userProfile = profileClient.getProfile(post.getUserId()).getResult();
            String displayName = (userProfile.getFirstName() != null ? userProfile.getFirstName() : "") +
                    (userProfile.getLastName() != null ? " " + userProfile.getLastName() : "");
            postResponse.setUsername(displayName.trim().isEmpty() ? null : displayName.trim());
        } catch (Exception e) {
            log.error("error while getting user profile: ", e);
            postResponse.setUsername(null);
        }

        return postResponse;
    }

    public PageResponse<PostResponse> getMyPosts(int page, int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = "default-user";
        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getName())) {
            userId = authentication.getName();
        }

        UserProfileResponse userProfile = null;

        try {
            userProfile = profileClient.getProfile(userId).getResult();
        } catch (Exception e) {
            log.error("error while getting user profile: ", e);
        }

        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = postRepository.findAllByUserId(userId, pageable);

        final String username;
        if (userProfile != null) {
            String displayName = (userProfile.getFirstName() != null ? userProfile.getFirstName() : "") +
                    (userProfile.getLastName() != null ? " " + userProfile.getLastName() : "");
            username = displayName.trim().isEmpty() ? null : displayName.trim();
        } else {
            username = null;
        }
        String currentUserId = getCurrentUserId();

        var postList = pageData.getContent().stream().map(post -> {
            var postResponse = postMapper.toPostResponse(post);

            // Set formatted created time
            if (post.getCreatedDate() != null) {
                postResponse.setCreated(dateTimeFormatter.format(post.getCreatedDate()));
            }

            // Set like/share status
            postResponse.setLiked(likeService.isPostLikedByUser(post.getId(), currentUserId));
            postResponse.setShared(shareService.isPostSharedByUser(post.getId(), currentUserId));

            postResponse.setUsername(username);
            return postResponse;
        }).toList();

        return PageResponse.<PostResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(postList)
                .build();
    }

    public PageResponse<PostResponse> getPostsByUserId(String userId, int page, int size) {
        UserProfileResponse userProfile = null;

        try {
            userProfile = profileClient.getProfile(userId).getResult();
        } catch (Exception e) {
            log.error("error while getting user profile: ", e);
        }

        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = postRepository.findAllByUserId(userId, pageable);

        final String username;
        if (userProfile != null) {
            String displayName = (userProfile.getFirstName() != null ? userProfile.getFirstName() : "") +
                    (userProfile.getLastName() != null ? " " + userProfile.getLastName() : "");
            username = displayName.trim().isEmpty() ? null : displayName.trim();
        } else {
            username = null;
        }
        String currentUserId = getCurrentUserId();

        var postList = pageData.getContent().stream().map(post -> {
            var postResponse = postMapper.toPostResponse(post);

            // Set formatted created time
            if (post.getCreatedDate() != null) {
                postResponse.setCreated(dateTimeFormatter.format(post.getCreatedDate()));
            }

            // Set like/share status
            postResponse.setLiked(likeService.isPostLikedByUser(post.getId(), currentUserId));
            postResponse.setShared(shareService.isPostSharedByUser(post.getId(), currentUserId));

            postResponse.setUsername(username);
            return postResponse;
        }).toList();

        return PageResponse.<PostResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(postList)
                .build();
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
