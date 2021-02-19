package com.example.bbs.dto;

import com.example.bbs.entity.Tag;
import lombok.Data;

@Data
public class TagDTO extends Tag {

    private Integer postCount;
}
