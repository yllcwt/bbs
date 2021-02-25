package com.example.bbs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bbs.entity.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryMapper extends BaseMapper<Category> {

    List<Category> listCategory(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize);
}
