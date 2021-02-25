package com.example.bbs.service;

import com.example.bbs.dto.PaginationDTO;
import com.example.bbs.dto.PostQueryCondition;
import com.example.bbs.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yl
 * @since 2021-02-21
 */
public interface CommentService extends IService<Comment> {

    PaginationDTO listComment(Integer pageIndex, Integer pageSize, PostQueryCondition postQueryCondition);
}
