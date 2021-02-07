package com.example.bbs.service;

import com.example.bbs.entity.Post;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yl
 * @since 2021-02-05
 */
public interface PostService extends IService<Post> {

//    void addPost(Post post);

    void insertOrUpdatePost(Post post);
}
