package com.tam.post.service;

import com.tam.post.dto.PageResponse;
import com.tam.post.dto.response.ShareResponse;
import com.tam.post.dto.response.UserProfileResponse;
import com.tam.post.entity.Post;
import com.tam.post.entity.Share;
import com.tam.post.repository.PostRepository;
import com.tam.post.repository.ShareRepository;
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
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShareService {
    ShareRepository shareRepository;
    PostRepository postRepository;
    ProfileClient profileClient;

    @Transactional
    public void sharePost(String postId) {
        String userId = getCurrentUserId();

        // Kiểm tra xem user đã share chưa
        if (shareRepository.existsByPostIdAndUserId(postId, userId)) {
            throw new RuntimeException("You already shared this post");
        }

        // Tạo share mới
        Share share = Share.builder()
                .postId(postId)
                .userId(userId)
                .createdDate(Instant.now())
                .build();

        shareRepository.save(share);

        // Tăng shareCount trong post
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        post.setShareCount(post.getShareCount() + 1);
        postRepository.save(post);
    }

    @Transactional
    public void unsharePost(String postId) {
        String userId = getCurrentUserId();

        // Tìm và xóa share
        Share share = shareRepository.findByPostIdAndUserId(postId, userId)
                .orElseThrow(() -> new RuntimeException("You haven't shared this post"));

        shareRepository.delete(share);

        // Giảm shareCount trong post
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        post.setShareCount(Math.max(0, post.getShareCount() - 1));
        postRepository.save(post);
    }

    public boolean isPostSharedByUser(String postId, String userId) {
        return shareRepository.existsByPostIdAndUserId(postId, userId);
    }

    public PageResponse<ShareResponse> getSharesByPostId(String postId, int page, int size) {
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = shareRepository.findByPostId(postId, pageable);

        var shareList = pageData.getContent().stream()
                .map(this::toShareResponse)
                .collect(Collectors.toList());

        return PageResponse.<ShareResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(shareList)
                .build();
    }

    private ShareResponse toShareResponse(Share share) {
        ShareResponse response = ShareResponse.builder()
                .id(share.getId())
                .postId(share.getPostId())
                .userId(share.getUserId())
                .createdDate(share.getCreatedDate())
                .build();

        // Set formatted created time
        if (share.getCreatedDate() != null) {
            response.setCreated(share.getCreatedDate().toString());
        }

        // Lấy username
        try {
            UserProfileResponse userProfile = profileClient.getProfile(share.getUserId()).getResult();
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

    public long getShareCount(String postId) {
        return shareRepository.countByPostId(postId);
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

