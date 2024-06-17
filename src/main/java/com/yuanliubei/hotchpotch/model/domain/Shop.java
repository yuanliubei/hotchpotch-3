package com.yuanliubei.hotchpotch.model.domain;

import com.yuanliubei.hotchpotch.config.CustomEntityAuditingListener;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yuanlb
 * @since 2024/6/9
 */
@EntityListeners({CustomEntityAuditingListener.class})
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "shop")
@Data
public class Shop extends BaseEntity{

    private String name;

    private String address;

    private Integer status;

    private String phone;

}
