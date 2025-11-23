package com.tam.post.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

/**
 * Base class cho Mongo document có audit fields
 */
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class AuditableBaseDocument {

    @CreatedDate
    @Field("createdDate")
    private Instant createdDate;

    @LastModifiedDate
    @Field("modifiedDate")
    private Instant modifiedDate;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String lastUpdatedBy;

    private int publicity; // 0: private, 1: friends, 2: public

    private String history;


    public void addHistoryEntry(String entry) {
        if (this.history == null) {
            this.history = entry;
        } else {
            this.history = this.history + "\n" + entry;
        }
    }
}

