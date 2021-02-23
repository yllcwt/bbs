package com.example.bbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.bbs.dto.PaginationDTO;
import com.example.bbs.dto.PostDTO;
import com.example.bbs.dto.PostQueryCondition;
import com.example.bbs.entity.*;
import com.example.bbs.mapper.CategoryPostRefMapper;
import com.example.bbs.mapper.PostMapper;
import com.example.bbs.mapper.TagMapper;
import com.example.bbs.mapper.UserMapper;
import com.example.bbs.service.PostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yl
 * @since 2021-02-05
 */
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {

    @Autowired
    private PostMapper postMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TagMapper tagMapper;

    @Override
    public void addPost(Post post) {
        postMapper.insert(post);
    }
    @Override
    public void insertOrUpdatePost(Post post) {
        if (post.getPostId() == null) {
            insertPost(post);
        }else {
//            updatePost(post);
        }
    }

    private void insertPost(Post post) {
        postMapper.insert(post);
    }

    @Override
    public PaginationDTO listPost(Integer pageIndex, Integer pageSize, PostQueryCondition postQueryCondition) {

        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalPage;


//        QueryWrapper<Post> postQueryWrapper = new QueryWrapper<>();
//        Integer totalCount = postMapper.selectCount(postQueryWrapper);
        Integer totalCount = postMapper.calculateCount(postQueryCondition);

        if (totalCount % pageSize == 0) {
            totalPage = totalCount / pageSize;
        } else {
            totalPage = totalCount / pageSize + 1;
        }

        if (pageIndex < 1) {
            pageIndex = 1;
        }
        if (pageIndex > totalPage) {
            pageIndex = totalPage;
        }

        paginationDTO.setPagination(totalPage, pageIndex);
        Integer offset = pageIndex < 1 ? 0 : pageSize * (pageIndex - 1);

        List<PostDTO> list = postMapper.selectPost(offset, pageSize, postQueryCondition);
        System.err.println(list);
//        List<PostDTO> listPostDTO = new ArrayList<>(list.size());
//
        for (PostDTO postDTO : list){
            List<Tag> tagList = tagMapper.findByPostId(postDTO.getPostId());
            postDTO.setTagList(tagList);
        }
        paginationDTO.setData(list);
        return paginationDTO;
    }

    @Override
    public PostDTO findPostByPostId(Integer postId) {
        PostDTO postDTO = postMapper.findPostByPostId(postId);
        List<Tag> tagList = tagMapper.findByPostId(postDTO.getPostId());
        postDTO.setTagList(tagList);
        return postDTO;
    }

    @Override
    public void addPostView(Integer postId) {
        postMapper.addPostView(postId);
    }

    @Override
    public void addPostCommentCount(Integer postId) {
        postMapper.addPostCommentCount(postId);
    }

    @Override
    public PaginationDTO listPostManage(Integer pageIndex, Integer pageSize, PostQueryCondition postQueryCondition) {

        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = postMapper.calculateAmount(postQueryCondition);

        paginationDTO.mySetPagination(pageIndex, pageSize, totalCount);

        Integer offset = paginationDTO.getPage() < 1 ? 0 : (paginationDTO.getPage()-1)*pageSize;
        List<Post> postList = postMapper.listPostManage(offset, pageSize, postQueryCondition);
        paginationDTO.setData(postList);
        return paginationDTO;
    }

}
