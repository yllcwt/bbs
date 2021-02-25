package com.example.bbs.mapper;

import com.example.bbs.dto.CommentDTO;
import com.example.bbs.dto.PostQueryCondition;
import com.example.bbs.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yl
 * @since 2021-02-21
 */
public interface CommentMapper extends BaseMapper<Comment> {

    Integer listCommentCount(@Param("postQueryCondition") PostQueryCondition postQueryCondition);

    List<CommentDTO> listComment(@Param("postQueryCondition") PostQueryCondition postQueryCondition, Integer offset, Integer pageSize);
}
