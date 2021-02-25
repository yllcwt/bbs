package com.example.bbs.service.impl;

import com.example.bbs.dto.CommentDTO;
import com.example.bbs.dto.PaginationDTO;
import com.example.bbs.dto.PostQueryCondition;
import com.example.bbs.entity.Comment;
import com.example.bbs.mapper.CommentMapper;
import com.example.bbs.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yl
 * @since 2021-02-21
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public PaginationDTO listComment(Integer pageIndex, Integer pageSize, PostQueryCondition postQueryCondition) {
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = commentMapper.listCommentCount(postQueryCondition);
        Integer offset = paginationDTO.mySetPagination(pageIndex, pageSize, totalCount);
        List<CommentDTO> commentList = commentMapper.listComment(postQueryCondition, offset, pageSize);
        paginationDTO.setData(commentList);
        return paginationDTO;
    }
}
