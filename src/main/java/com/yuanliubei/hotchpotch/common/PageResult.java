package com.yuanliubei.hotchpotch.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yuanlb
 * @since 2023/4/9
 */
@Getter
@Setter
public class PageResult<T> {

    // 当前页的结果集数据:查询
    private List<T> listData;

    // 总数据条数:查询
    private Integer totalCount;

    //当前页
    private Integer currentPage = 1;

    //每页条数，默认10
    private Integer pageSize = 10;

    // 上一页
    private Integer prevPage;

    // 下一页
    private Integer nextPage;

    // 总页数
    private Integer totalPage;


    // 如果总数据条数为0,返回一个空集
    public static <T> PageResult<T> empty(Integer pageSize) {
        return new PageResult<>(new ArrayList<>(), 0, 1, pageSize);
    }

    public int getTotalPage() {
        return totalPage == 0 ? 1 : totalPage;
    }

    public PageResult(List<T> listData, Integer totalCount, Integer currentPage,
                      Integer pageSize) {
        this.listData = listData;
        this.totalCount = totalCount;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        // ----------------------------------------
        this.totalPage = this.totalCount % this.pageSize == 0 ?
                this.totalCount / this.pageSize : this.totalCount / this.pageSize + 1;

        this.prevPage = Math.max(this.currentPage - 1, 1);
        this.nextPage = this.currentPage + 1 <= this.totalPage ? this.currentPage + 1
                : this.totalPage;
    }


    public static <T> PageResult<T> build(Page<T> page) {
        if (!page.hasContent()) {
            return PageResult.empty(page.getSize());
        }
        return new PageResult<>(page.getContent(), (int) page.getTotalElements(), page.getPageable().getPageNumber() + 1, page.getPageable().getPageSize());
    }
}
