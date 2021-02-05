package com.example.bbs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class TagPostRef implements Serializable {

    @TableId(value = "tag_post_ref", type = IdType.AUTO)
    private Integer id;

    private Integer tagId;

    private Integer postId;
}
