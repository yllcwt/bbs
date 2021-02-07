package com.example.bbs.service.impl;

import com.example.bbs.entity.Post;
import com.example.bbs.mapper.PostMapper;
import com.example.bbs.service.PostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

//    @Override
//    public void addPost(Post post) {
//        postMapper.insert(post);
//    }
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
}
