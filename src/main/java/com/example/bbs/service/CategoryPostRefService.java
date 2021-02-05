package com.example.bbs.service;



import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bbs.entity.CategoryPostRef;

public interface CategoryPostRefService extends IService<CategoryPostRef> {
    void addCategoryPostRef(CategoryPostRef categoryPostRef);
}
