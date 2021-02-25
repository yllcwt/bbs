package com.example.bbs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author yl
 * @since 2021-02-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Comment implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "comment_id", type = IdType.AUTO)
    private Integer commentId;

    private Integer postId;

    /**
     * 评论人的userId
     */
    private Integer userId;

    private String commentContent;

    private Integer commentParentId=0;

    private Integer acceptUserId;

    private Integer commentLike;

    private Date commentTime;

    private String commentOriginContent;

    @TableField(exist = false)
    private Post post;

    /**
     * 回帖人
     */
    @TableField(exist = false)
    private User user;

    /**
     * 当前回帖下的所有子回帖
     */
    @TableField(exist = false)
    private List<Comment> childComments;
}
