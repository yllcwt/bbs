package com.example.bbs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bbs.dto.PaginationDTO;
import com.example.bbs.entity.Category;

public interface CategoryService extends IService<Category> {
    PaginationDTO listCategory(Integer pageIndex, Integer pageSize);
}
