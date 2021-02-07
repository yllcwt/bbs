package com.example.bbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
    public void addOrSelectCategoryPostRef(CategoryPostRef categoryPostRef) {
        LambdaQueryWrapper<CategoryPostRef> categoryPostRefLambdaQueryWrapper = Wrappers.lambdaQuery();
        categoryPostRefLambdaQueryWrapper.eq(CategoryPostRef::getPostId, categoryPostRef.getPostId());
        categoryPostRefLambdaQueryWrapper.eq(CategoryPostRef::getCategoryId, categoryPostRef.getCategoryId());
        CategoryPostRef originCategoryPostRef = getOne(categoryPostRefLambdaQueryWrapper);
        if(originCategoryPostRef == null){
            categoryPostRefMapper.insert(categoryPostRef);
        }
    }
}
