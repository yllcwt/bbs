package com.example.bbs.controller;

import com.example.bbs.dto.JsonResult;
import com.example.bbs.entity.*;
import com.example.bbs.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private TagService tagService;
    @Autowired
    private CategoryPostRefService categoryPostRefService;
    @Autowired
    private TagPostRefService tagPostRefService;

    @GetMapping("test")
    public String testUser(Model model){
        Post post = postService.getById(20);
        model.addAttribute("post", post);
        return "test";
    }

    @PostMapping("/savePost")
    @ResponseBody
    public JsonResult savePost(Post post,
                               HttpServletRequest request,
                               @RequestParam("categoryId") Integer categoryId,
                               @RequestParam("tags") String tags){
//        Integer categoryId = Integer.parseInt(originCategoryId);
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
            post.setPostTime(new Date());
            post.setPostView(0);
            post.setUserId(user.getUserId());
        }

        postService.insertOrUpdatePost(post);

        //分类
        CategoryPostRef categoryPostRef = new CategoryPostRef();
        //这里没有new 进行赋值
        categoryPostRef.setCategoryId(categoryId);
        categoryPostRef.setPostId(post.getPostId());
        categoryPostRefService.addOrSelectCategoryPostRef(categoryPostRef);

        if(StringUtils.isNotEmpty(tags)){
            List<Tag> list = tagService.addOrSelectTag(StringUtils.deleteWhitespace(tags));
            tagPostRefService.addOrSelectTagPostRef(list,post.getPostId());
        }

        return JsonResult.success("发布成功！");
    }

}
