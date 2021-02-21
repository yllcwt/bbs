package com.example.bbs.controller;


import cn.hutool.http.HtmlUtil;
import com.example.bbs.dto.JsonResult;
import com.example.bbs.entity.Comment;
import com.example.bbs.entity.Post;
import com.example.bbs.entity.User;
import com.example.bbs.service.CommentService;
import com.example.bbs.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yl
 * @since 2021-02-21
 */
@Controller
public class CommentController {

    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;

    @PostMapping("/comment")
    @ResponseBody
    public JsonResult addComment(@RequestParam("postId") Integer postId,
                             @RequestParam(value = "commentId", required = false) Integer commentId,
                             @RequestParam("commentContent") String commentContent,
                             HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return JsonResult.error("请先登录！");
        }

        Post post = postService.getById(postId);
        if (post == null) {
            return JsonResult.error("帖子不存在！");
        }

        Comment comment = new Comment();
        if (commentId != null) {

        } else {
            //回复帖子
            comment.setCommentContent(HtmlUtil.escape(commentContent));
            comment.setAcceptUserId(post.getUserId());
            comment.setCommentParentId(0);
        }
        comment.setCommentTime(new Date());
        comment.setUserId(user.getUserId());
        comment.setPostId(postId);

        commentService.save(comment);
        return JsonResult.success("回复帖子成功！");
    }

}

