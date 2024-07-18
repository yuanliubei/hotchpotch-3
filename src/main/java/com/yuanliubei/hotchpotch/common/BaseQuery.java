package com.yuanliubei.hotchpotch.common;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.yuanliubei.hotchpotch.model.domain.BaseEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author yuanlb
 * @since 2023/4/9
 */
@Data
public abstract class BaseQuery<T extends BaseEntity> {

    private Long id;
    private List<Long> ids;

    protected LocalDateTime createTimeMin;
    protected LocalDateTime createTimeMax;
    protected LocalDateTime updateTimeMin;
    protected LocalDateTime updateTimeMax;


    public abstract LambdaQueryWrapper<T> buildExample();

}
