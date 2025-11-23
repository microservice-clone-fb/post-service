package com.tam.post.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentResponse {
    String id;
    String postId;
    String userId;
    String username;
    String content;
    String parentCommentId;
    String created;
    Instant createdDate;
    Instant modifiedDate;
    List<CommentResponse> replies; // Danh sách reply cho comment này
}

