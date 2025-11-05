package com.tam.post.utils;

import com.tam.post.entity.AuditableBaseDocument;
import com.tam.post.service.AuditServices;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * Mongo Event Listener để set audit fields
 */
@Component
@RequiredArgsConstructor
public class AuditListener extends AbstractMongoEventListener<AuditableBaseDocument> {

    private final AuditServices auditService;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<AuditableBaseDocument> event) {
        AuditableBaseDocument entity = event.getSource();
        Instant now = Instant.now();
        String currentUser = auditService.getCurrentUsername();

        if (entity.getCreatedAt() == null) {
            entity.setCreatedAt(now);
            entity.setCreatedBy(currentUser);

            String historyEntry = auditService.createHistoryEntry(currentUser, "CREATED", now);
            entity.addHistoryEntry(historyEntry);
        }

        entity.setLastUpdatedAt(now);
        entity.setLastUpdatedBy(currentUser);

        String historyEntry = auditService.createHistoryEntry(currentUser, "UPDATED", now);
        entity.addHistoryEntry(historyEntry);
    }
}
