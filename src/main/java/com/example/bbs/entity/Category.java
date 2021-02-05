package com.example.bbs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class Category implements Serializable {

    @TableId(value = "category", type = IdType.AUTO)
    private Integer id;

    private Integer categoryId;

    private String explanation;

}