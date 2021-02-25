package com.example.bbs.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by codedrinker on 2019/5/14.
 */
@Data
public class PaginationDTO<T> {
    private List<T> data;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;
    private Integer page;
    private List<Integer> pages = new ArrayList<>();
    private Integer totalPage;

    public Integer mySetPagination(Integer pageIndex, Integer pageSize, Integer totalCount) {
        if (totalCount % pageSize == 0) {
            this.totalPage = totalCount / pageSize;
        } else {
            this.totalPage = totalCount / pageSize + 1;
        }

        if (pageIndex < 1) {
            pageIndex = 1;
        }
        if (pageIndex > this.totalPage) {
            pageIndex = this.totalPage;
        }
        this.setPage(pageIndex);

        // 是否展示上一页
        if (page == 1) {
            this.showPrevious = false;
        } else {
            this.showPrevious = true;
        }

        // 是否展示下一页
        if (page == totalPage) {
            this.showNext = false;
        } else {
            this.showNext = true;
        }

        this.showFirstPage = this.page != 1;
        this.showEndPage = this.page != this.totalPage;
        return this.getPage() < 1 ? 0 : (this.getPage()-1)*pageSize;
    }

    public void setPagination(Integer totalPage, Integer page) {
        this.totalPage = totalPage;
        this.page = page;

        pages.add(page);
        for (int i = 1; i <= 3; i++) {
            if (page - i > 0) {
                pages.add(0, page - i);
            }

            if (page + i <= totalPage) {
                pages.add(page + i);
            }
        }

        // 是否展示上一页
        if (page == 1) {
            showPrevious = false;
        } else {
            showPrevious = true;
        }

        // 是否展示下一页
        if (page == totalPage) {
            showNext = false;
        } else {
            showNext = true;
        }

        // 是否展示第一页
        if (pages.contains(1)) {
            showFirstPage = false;
        } else {
            showFirstPage = true;
        }

        // 是否展示最后一页
        if (pages.contains(totalPage)) {
            showEndPage = false;
        } else {
            showEndPage = true;
        }
    }
}
