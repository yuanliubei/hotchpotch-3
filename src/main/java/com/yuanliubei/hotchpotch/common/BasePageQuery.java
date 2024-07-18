package com.yuanliubei.hotchpotch.common;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuanliubei.hotchpotch.model.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author yuanlb
 * @since 2024/7/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class BasePageQuery<T extends BaseEntity> extends BaseQuery<T>
        implements Serializable {

    protected BasePageQuery.PageInfo pageInfo = new BasePageQuery.PageInfo();

    public IPage<T> toPageRequest() {
        PageInfo pageInfo = this.getPageInfo();
        return new Page<>(pageInfo.getPageNumber(), pageInfo.getPageSize());
    }

    @Getter
    @Setter
    public static class PageInfo {

        private Integer pageNumber;

        private Integer pageSize;

        public PageInfo() {
            this.pageNumber = 1;
            this.pageSize = 10;
        }

        public PageInfo(Integer pageNumber, Integer pageSize) {
            this.pageNumber = pageNumber;
            this.pageSize = pageSize;
        }

        public Integer getStart() {
            return (this.pageNumber - 1) * this.pageSize;
        }
    }
}
