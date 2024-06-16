package com.yuanliubei.hotchpotch.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author yuanlb
 * @since 2024/6/10
 */
@EnableJpaRepositories(basePackages = { "com.yuanliubei.hotchpotch.model.dao" })
@EntityScan(basePackages = { "com.yuanliubei.hotchpotch.model.domain" })
@Configuration
public class JpaConfig {

    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }
}
