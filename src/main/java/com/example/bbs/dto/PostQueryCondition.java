package com.example.bbs.dto;

import lombok.Data;

@Data
public class PostQueryCondition {

    private Integer categoryId;

    private Integer tagId;

    private String keywords;

    private Integer userId;
}
