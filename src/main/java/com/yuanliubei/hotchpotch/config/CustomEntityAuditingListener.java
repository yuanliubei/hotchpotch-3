package com.yuanliubei.hotchpotch.config;

import com.yuanliubei.hotchpotch.model.domain.BaseEntity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

/**
 * @author yuanlb
 * @since 2024/6/17
 */
public class CustomEntityAuditingListener {

    private static final Long USER_ID = 10L;

    @PrePersist
    private void prePersist(BaseEntity entity) {
        entity.setCreateBy(USER_ID);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateBy(entity.getCreateBy());
        entity.setUpdateTime(entity.getCreateTime());
        entity.setDeleted(Boolean.FALSE);
    }

    @PreUpdate
    private void preUpdate(BaseEntity entity) {
        entity.setUpdateBy(USER_ID);
        entity.setUpdateTime(LocalDateTime.now());
    }
}
