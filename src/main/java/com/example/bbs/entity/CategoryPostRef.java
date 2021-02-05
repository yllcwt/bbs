package com.example.bbs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class CategoryPostRef implements Serializable {

    @TableId(value = "category_post_ref", type = IdType.AUTO)

    private Integer id;

    private Integer categoryId;

    private Integer postId;
}
