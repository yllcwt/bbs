package com.example.bbs.dto;

import lombok.Data;

@Data
public class PostQueryCondition {

    private Integer categoryId;

    private Integer tagId;

    private String keywords;

    private Integer userId;

    private String sort;

    private String order;

    private String searchType;

    private Integer buttonType;
}
