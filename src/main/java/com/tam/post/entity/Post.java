package com.tam.post.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;

@Getter
@Setter
@Document(value = "post")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Post extends AuditableBaseDocument {
    @MongoId
    String id;
    String userId;
    String content;
    String mediaUrl; // hình ảnh, video kèm theo

    @Builder
    public Post(Instant createdAt, Instant lastUpdatedAt, String createdBy, String lastUpdatedBy,
            int publicity, String history, String id, String userId, String content, String mediaUrl) {
        super(createdAt, lastUpdatedAt, createdBy, lastUpdatedBy, publicity, history);
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.mediaUrl = mediaUrl;
    }
}
