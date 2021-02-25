package com.example.bbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bbs.dto.PaginationDTO;
import com.example.bbs.entity.Category;
import com.example.bbs.mapper.CategoryMapper;
import com.example.bbs.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yl
 * @since 2021-02-07
 */
@Service
public class CategoryServiceImp extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public PaginationDTO listCategory(Integer pageIndex, Integer pageSize) {
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = categoryMapper.selectCount(new QueryWrapper<>());
        Integer offset = paginationDTO.mySetPagination(pageIndex,pageSize,totalCount);
        List<Category> categoryList = categoryMapper.listCategory(offset, pageSize);
        paginationDTO.setData(categoryList);
        return paginationDTO;
    }
}
