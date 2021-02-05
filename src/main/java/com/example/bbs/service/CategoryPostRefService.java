package com.example.bbs.service;

import com.example.bbs.entity.CategoryPostRef;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yl
 * @since 2021-02-05
 */
public interface CategoryPostRefService extends IService<CategoryPostRef> {

    void addCategoryPostRef(CategoryPostRef categoryPostRef);
}
