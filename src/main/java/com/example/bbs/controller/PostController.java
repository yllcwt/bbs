package com.example.bbs.controller;

import com.example.bbs.dto.JsonResult;
import com.example.bbs.entity.Post;
import com.example.bbs.entity.User;
import com.example.bbs.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/savePost")
    public JsonResult savePost(Post post,
                               HttpServletRequest request,
                               @RequestParam("categoryId") Integer categoryId,
                               @RequestParam("tags") String tags){
        //检查标签合法性
        String[] originTags = tags.split(",");
        if(originTags.length > 5){
            return JsonResult.error("每个帖子最多5个标签！");
        }
        for (String tag : originTags) {
            if(tag.length() > 20){
                return JsonResult.error("每个标签长度最多为20个字符！");
            }
        }

        Post originPost = null;
        //获取登录用户
        User user = (User)request.getSession().getAttribute("user");
        post.setUserId(user.getUserId());
        //
        if(post.getPostId() != null){
            originPost = postService.getById(post.getPostId());

            post.setPostCommentCount(originPost.getPostCommentCount());
            post.setPostLike(originPost.getPostLike());
            post.setPostTime(originPost.getPostTime());
            post.setPostView(originPost.getPostView());
            post.setPostId(originPost.getPostId());
        } else {
            post.setPostCommentCount(0);
            post.setPostLike(0);
            post.setPostTime(System.currentTimeMillis());
            post.setPostView(0);
            post.setUserId(user.getUserId());
        }
        //分类标签
        Category category = new Category();
        category.setCategoryId(categoryId);

        return JsonResult.success("");
    }

}
