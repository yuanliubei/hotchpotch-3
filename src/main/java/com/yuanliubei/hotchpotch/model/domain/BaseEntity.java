package com.yuanliubei.hotchpotch.model.domain;

import com.yuanliubei.hotchpotch.config.CustomEntityAuditingListener;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;


/**
 * @author yuanlb
 * @since 2024/5/23
 */
@Data
@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedBy
    private Long createBy;

    @CreatedDate
    private LocalDateTime createTime;

    @LastModifiedBy
    private Long updateBy;

    @LastModifiedDate
    private LocalDateTime updateTime;

    private Boolean deleted;


}
