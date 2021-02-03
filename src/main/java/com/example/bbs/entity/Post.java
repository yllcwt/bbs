package com.example.bbs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class Post implements Serializable {

    @TableId(value = "post_id", type = IdType.AUTO)
    private Integer postId;

    private Integer userId;

    private String postTitle;

    private String postContent;

    private Integer postTime;

    private Integer postView;

    private Integer postLike;

    private Integer postCommentCount;
}
