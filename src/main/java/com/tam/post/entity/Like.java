package com.tam.post.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(value = "like")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Like {
    @MongoId
    String id;
    String postId;
    String userId;
    Instant createdDate;
}

