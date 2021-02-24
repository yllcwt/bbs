package com.example.bbs.service;

import com.example.bbs.dto.PaginationDTO;
import com.example.bbs.dto.PostDTO;
import com.example.bbs.dto.PostQueryCondition;
import com.example.bbs.entity.Category;
import com.example.bbs.entity.Post;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yl
 * @since 2021-02-05
 */
public interface PostService extends IService<Post> {

    void addPost(Post post);

    @Deprecated
    void insertOrUpdatePost(Post post);

    PaginationDTO listPost(Integer pageIndex, Integer pageSize, PostQueryCondition postQueryCondition);

    PostDTO findPostByPostId(Integer postId);

    void addPostView(Integer postId);

    void addPostCommentCount(Integer postId);

    PaginationDTO listPostManage(Integer pageIndex, Integer pageSize, PostQueryCondition postQueryCondition);
}
