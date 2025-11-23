package com.tam.post.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document(value = "post")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Post extends AuditableBaseDocument {
    @MongoId
    String id;
    String userId;
    String content;
    String mediaUrl; // hình ảnh, video kèm theo

    @Builder.Default
    int likeCount = 0;

    @Builder.Default
    int commentCount = 0;

    @Builder.Default
    int shareCount = 0;
}
