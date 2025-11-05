package com.tam.post.configuration;

import com.tam.post.service.AuditServices;
import com.tam.post.utils.AuditListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//@Deprecated
@Configuration
public class AuditConfiguration {

    @Bean
    @ConditionalOnMissingBean(AuditServices.class)
    public AuditServices auditService() {
        return new AuditServices();
    }

    @Bean
    @ConditionalOnMissingBean(AuditListener.class)
    public AuditListener auditEntityListener(AuditServices auditServices) {
        return new AuditListener(auditServices);
    }
}