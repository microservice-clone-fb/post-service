package com.tam.post.service;

import com.tam.post.dto.PageResponse;
import com.tam.post.dto.response.LikeResponse;
import com.tam.post.dto.response.UserProfileResponse;
import com.tam.post.entity.Like;
import com.tam.post.entity.Post;
import com.tam.post.repository.LikeRepository;
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

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LikeService {
    LikeRepository likeRepository;
    PostRepository postRepository;
    ProfileClient profileClient;

    @Transactional
    public void likePost(String postId) {
        String userId = getCurrentUserId();

        // Kiểm tra xem user đã like chưa
        if (likeRepository.existsByPostIdAndUserId(postId, userId)) {
            throw new RuntimeException("You already liked this post");
        }

        // Tạo like mới
        Like like = Like.builder()
                .postId(postId)
                .userId(userId)
                .createdDate(Instant.now())
                .build();

        likeRepository.save(like);

        // Tăng likeCount trong post
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        post.setLikeCount(post.getLikeCount() + 1);
        postRepository.save(post);
    }

    @Transactional
    public void unlikePost(String postId) {
        String userId = getCurrentUserId();

        // Tìm và xóa like
        Like like = likeRepository.findByPostIdAndUserId(postId, userId)
                .orElseThrow(() -> new RuntimeException("You haven't liked this post"));

        likeRepository.delete(like);

        // Giảm likeCount trong post
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        post.setLikeCount(Math.max(0, post.getLikeCount() - 1));
        postRepository.save(post);
    }

    public boolean isPostLikedByUser(String postId, String userId) {
        return likeRepository.existsByPostIdAndUserId(postId, userId);
    }

    public long getLikeCount(String postId) {
        return likeRepository.countByPostId(postId);
    }

    public PageResponse<LikeResponse> getLikesByPostId(String postId, int page, int size) {
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = likeRepository.findByPostId(postId, pageable);

        var likeList = pageData.getContent().stream()
                .map(this::toLikeResponse)
                .collect(Collectors.toList());

        return PageResponse.<LikeResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(likeList)
                .build();
    }

    private LikeResponse toLikeResponse(Like like) {
        LikeResponse response = LikeResponse.builder()
                .id(like.getId())
                .postId(like.getPostId())
                .userId(like.getUserId())
                .createdDate(like.getCreatedDate())
                .build();

        // Set formatted created time
        if (like.getCreatedDate() != null) {
            response.setCreated(like.getCreatedDate().toString()); // or use formatter if available
        }

        // Lấy username
        try {
            UserProfileResponse userProfile = profileClient.getProfile(like.getUserId()).getResult();
            String username = null;
            if (userProfile != null) {
                username = (userProfile.getFirstName() != null ? userProfile.getFirstName() : "") + " " +
                           (userProfile.getLastName() != null ? userProfile.getLastName() : "");
                username = username.trim();
                if (username.isEmpty()) username = null;
            }
            response.setUsername(username);
        } catch (Exception e) {
            log.error("Error while getting user profile: ", e);
            response.setUsername(null);
        }

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

