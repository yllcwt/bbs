package com.example.bbs.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.bbs.dto.JsonResult;
import com.example.bbs.dto.PaginationDTO;
import com.example.bbs.dto.PostDTO;
import com.example.bbs.dto.PostQueryCondition;
import com.example.bbs.entity.*;
import com.example.bbs.service.*;
import com.example.bbs.util.CommentUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;

    @GetMapping("test")
    public String testUser(Model model){
        Post post = postService.getById(19);
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
            post.setPostTop(originPost.getPostTop());
        } else {
            post.setPostCommentCount(0);
            post.setPostLike(0);
            post.setPostTime(new Date());
            post.setPostView(0);
            post.setUserId(user.getUserId());
            post.setPostTop(0);
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

    @GetMapping("/post_publish")
    public String postPublish(Model model){
        List<Category> categoryList = categoryService.list();
        model.addAttribute("categoryList", categoryList);
        return "post_publish";
    }

    @GetMapping("/post/{id}")
    public String postView(@PathVariable("id") Integer postId,
                           Model model){
        PostDTO postDTO = postService.findPostByPostId(postId);
        model.addAttribute("postDTO", postDTO);

        LambdaQueryWrapper<Comment> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Comment::getPostId, postId);
        List<Comment> commentList = commentService.list(wrapper);

        for (Comment comment : commentList) {
            User commentUser = userService.getById(comment.getUserId());
            comment.setUser(commentUser);
        }

        commentList = CommentUtil.getComments(commentList);
        System.err.println(commentList);
        model.addAttribute("commentList", commentList);

        //增加帖子点击量
        postService.addPostView(postId);
        return "post_view";
    }

    @GetMapping("/postManage")
    public String postManage(Model model,
                             @RequestParam(value = "pageIndex",defaultValue = "1") Integer pageIndex,
                             @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                             @RequestParam(value = "keywords", defaultValue = "") String keywords,
                             @RequestParam(value = "sort", defaultValue = "post_top") String sort,
                             @RequestParam(value = "order", defaultValue = "desc") String order,
                             @RequestParam(value = "searchType", defaultValue = "post_title") String searchType) {
        PostQueryCondition postQueryCondition = new PostQueryCondition();
        postQueryCondition.setKeywords(keywords);
        postQueryCondition.setOrder(order);
        postQueryCondition.setSearchType(searchType);
        postQueryCondition.setSort(sort);
        PaginationDTO paginationDTO = postService.listPostManage(pageIndex, pageSize, postQueryCondition);
        System.err.println(paginationDTO.getData());
        model.addAttribute("paginationDTO", paginationDTO);
        return "post_list";
    }

    @GetMapping("/postBatchDelete")
    @ResponseBody
    public JsonResult postBatchDelete(@RequestParam("ids") String ids){
        System.err.println(ids);
        return JsonResult.success("111");
    }
}
