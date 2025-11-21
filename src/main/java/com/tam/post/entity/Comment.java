package com.tam.post.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document(value = "comment")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment extends AuditableBaseDocument {
    @MongoId
    String id;
    String postId;
    String userId;
    String content;
    String parentCommentId; // null nếu là comment gốc, có giá trị nếu là reply
    
}
