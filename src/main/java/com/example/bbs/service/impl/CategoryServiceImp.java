package com.example.bbs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bbs.entity.Category;
import com.example.bbs.mapper.CategoryMapper;
import com.example.bbs.service.CategoryService;
import org.springframework.stereotype.Service;


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

}
