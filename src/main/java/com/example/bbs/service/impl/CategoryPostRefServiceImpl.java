package com.example.bbs.service.impl;

import com.example.bbs.entity.CategoryPostRef;
import com.example.bbs.mapper.CategoryPostRefMapper;
import com.example.bbs.service.CategoryPostRefService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yl
 * @since 2021-02-05
 */
@Service
public class CategoryPostRefServiceImpl extends ServiceImpl<CategoryPostRefMapper, CategoryPostRef> implements CategoryPostRefService {

    @Autowired
    private CategoryPostRefMapper categoryPostRefMapper;

    @Override
    public void addCategoryPostRef(CategoryPostRef categoryPostRef) {

    }
}
