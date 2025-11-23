package com.tam.post.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostResponse {
    String id;
    String content;
    String mediaUrl;
    String userId;
    String username;
    String created;
    Instant createdAt;
    Instant lastUpdatedAt;
    Instant createdDate;
    Instant modifiedDate;

    // Thống kê
    int likeCount;
    int commentCount;
    int shareCount;

    // Trạng thái của user hiện tại
    boolean isLiked; // User hiện tại đã like post này chưa
    boolean isShared; // User hiện tại đã share post này chưa
}
