package com.yuanliubei.hotchpotch.model.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


/**
 * @author yuanlb
 * @since 2024/5/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "user_v2")
public class UserV2 extends BaseEntity{

    private String name;

    private Integer age;

}
