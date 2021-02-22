package com.example.bbs.controller;


import cn.hutool.http.HtmlUtil;
import com.example.bbs.dto.JsonResult;
import com.example.bbs.entity.Comment;
import com.example.bbs.entity.Post;
import com.example.bbs.entity.User;
import com.example.bbs.service.CommentService;
import com.example.bbs.service.PostService;
import com.example.bbs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;


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
    @Autowired
    private UserService userService;

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
            //回复回帖
            Comment parentComment = commentService.getById(commentId);
            if (parentComment == null || !Objects.equals(parentComment.getPostId(), postId)) {
                return JsonResult.error("回帖不存在！");
            }
            User parentUser = userService.getById(parentComment.getUserId());
            if (parentUser != null) {
                String lastContent = "<a href='#comment-id-"+parentComment.getCommentId()+"'>@"+parentUser.getUserName()+"</a> ";
                comment.setCommentContent(lastContent+HtmlUtil.escape(commentContent));
                comment.setAcceptUserId(parentComment.getUserId());
                comment.setCommentParentId(parentComment.getCommentId());
            }
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

        //修改评论数
        postService.addPostCommentCount(postId);
        return JsonResult.success("回复成功！");
    }

}

