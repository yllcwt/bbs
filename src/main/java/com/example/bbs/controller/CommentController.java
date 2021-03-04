package com.example.bbs.controller;


import cn.hutool.http.HtmlUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.bbs.dto.JsonResult;
import com.example.bbs.dto.PaginationDTO;
import com.example.bbs.dto.PostQueryCondition;
import com.example.bbs.entity.Comment;
import com.example.bbs.entity.CommentLike;
import com.example.bbs.entity.Post;
import com.example.bbs.entity.User;
import com.example.bbs.exception.MyBusinessException;
import com.example.bbs.service.CommentLikeService;
import com.example.bbs.service.CommentService;
import com.example.bbs.service.PostService;
import com.example.bbs.service.UserService;
import com.example.bbs.util.CommentUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    @Autowired
    private CommentLikeService commentLikeService;

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
                String lastContent = "<a href='#comment-id-"+parentComment.getCommentId()+"'>@"+parentUser.getUserDisplayName()+"</a> ";
                comment.setCommentContent(lastContent+HtmlUtil.escape(commentContent));
                comment.setAcceptUserId(parentComment.getUserId());
                comment.setCommentParentId(parentComment.getCommentId());
                comment.setCommentOriginContent(HtmlUtil.escape(commentContent));
            }
        } else {
            //回复帖子
            comment.setCommentContent(HtmlUtil.escape(commentContent));
            comment.setCommentOriginContent(HtmlUtil.escape(commentContent));
            comment.setAcceptUserId(post.getUserId());
            comment.setCommentParentId(0);
            comment.setCommentLike(0);
        }
        comment.setCommentTime(new Date());
        comment.setUserId(user.getUserId());
        comment.setPostId(postId);

        commentService.save(comment);

        //修改评论数
        postService.addPostCommentCount(postId);
        return JsonResult.success("回复成功！");
    }

    @GetMapping("/commentList")
    public String commentList(Model model,
                              @RequestParam(value = "pageIndex",defaultValue = "1") Integer pageIndex,
                              @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                              @RequestParam(value = "keywords",defaultValue = "") String keywords,
                              @RequestParam(value = "sort", defaultValue = "comment_time") String sort,
                              @RequestParam(value = "order", defaultValue = "desc") String order) {
        PostQueryCondition postQueryCondition = new PostQueryCondition();
        postQueryCondition.setKeywords(keywords);
        postQueryCondition.setOrder(order);
        postQueryCondition.setSort(sort);
        PaginationDTO paginationDTO = commentService.listComment(pageIndex, pageSize, postQueryCondition);
        model.addAttribute("paginationDTO", paginationDTO);
        model.addAttribute("pageIndex", pageIndex);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("keywords", keywords);
        model.addAttribute("sort", sort);
        model.addAttribute("order", order);
        return "comment_list";
    }
    @PostMapping("/commentDelete")
    @ResponseBody
    public JsonResult commentDelete(@RequestParam("commentId") Integer commentId) {
        Comment comment = commentService.getById(commentId);
        if(comment.getCommentParentId() == 0) {
            LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = Wrappers.lambdaQuery();
            commentLambdaQueryWrapper.eq(Comment::getCommentParentId, commentId);
            List<Comment> commentList = commentService.list(commentLambdaQueryWrapper);
            for (Comment childComment : commentList) {
                commentService.removeById(childComment.getCommentId());
            }
            deleteCommentRelation(comment);
            commentService.removeById(commentId);
        } else {
            commentService.removeById(commentId);
        }
        return JsonResult.success("删除成功！");
    }
    @GetMapping("/commentBatchDelete")
    @ResponseBody
    public JsonResult commentBatchDelete(@RequestParam("ids") List<Integer> ids,
                                         HttpServletRequest request) {
        List<Comment> commentList = commentService.listByIds(ids);
        for (Comment comment : commentList) {
            if(comment.getCommentParentId() == 0) {
                deleteCommentRelation(comment);
            }
            basicCheck(comment, request);
        }
        commentService.removeByIds(ids);
        return JsonResult.success("删除成功！");
    }

    @GetMapping("/commentMine")
    public String commentMine(Model model,
                              @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                              @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                              @RequestParam(value = "keywords", defaultValue = "") String keywords,
                              @RequestParam(value = "sort", defaultValue = "comment_time") String sort,
                              @RequestParam(value = "order", defaultValue = "desc") String order,
                              HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        PostQueryCondition postQueryCondition = new PostQueryCondition();
        postQueryCondition.setKeywords(keywords);
        postQueryCondition.setOrder(order);
        postQueryCondition.setSort(sort);
        postQueryCondition.setUserId(user.getUserId());
        PaginationDTO paginationDTO = commentService.listComment(pageIndex, pageSize, postQueryCondition);
        System.err.println(paginationDTO);
        model.addAttribute("paginationDTO", paginationDTO);
        model.addAttribute("pageIndex", pageIndex);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("keywords", keywords);
        model.addAttribute("sort", sort);
        model.addAttribute("order", order);
        return "comment_mine";
    }

    @PostMapping("/commentLike")
    @ResponseBody
    public JsonResult commentLike(@RequestParam("commentId") Integer commentId,
                                  @RequestParam("commentLike") Integer commentLike,
                                  HttpServletRequest request) {
        User user = (User)request.getSession().getAttribute("user");
        LambdaQueryWrapper<CommentLike> commentLikeLambdaQueryWrapper = Wrappers.lambdaQuery();
        commentLikeLambdaQueryWrapper.eq(CommentLike::getUserId, user.getUserId())
                                     .eq(CommentLike::getCommentId, commentId);
        CommentLike commentLikeCount = commentLikeService.getOne(commentLikeLambdaQueryWrapper);

        if (commentLikeCount == null) {
            CommentLike c = new CommentLike(commentId, user.getUserId());
            commentLikeService.save(c);

//            Comment comment = new Comment();
//            comment.setCommentLike(commentLike+1);
//            comment.setCommentId(commentId);
            Comment comment = commentService.getById(commentId);
            comment.setCommentLike(commentLike+1);
            commentService.updateById(comment);
            return JsonResult.success("点赞成功！");
        } else {
            commentLikeService.remove(commentLikeLambdaQueryWrapper);

//            Comment comment = new Comment();
//            comment.setCommentLike(commentLike-1);
//            comment.setCommentId(commentId);
            Comment comment = commentService.getById(commentId);
            comment.setCommentLike(commentLike-1);
            commentService.updateById(comment);
            return JsonResult.error("取消点赞成功！");
        }

    }

    private void deleteCommentRelation(Comment comment) {
        //删除评论点赞关系
        LambdaQueryWrapper<CommentLike> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(CommentLike::getCommentId, comment.getCommentId());
        commentLikeService.remove(wrapper);
        //文章点赞数-1
        Post post = postService.getById(comment.getPostId());
        post.setPostCommentCount(post.getPostCommentCount()-1);
        postService.updateById(post);
    }

    private void basicCheck(Comment comment, HttpServletRequest request) {
        User user = (User)request.getSession().getAttribute("user");

        if(user == null) {
            throw new MyBusinessException("请先登录！");
        }
        if(comment == null) {
            throw new MyBusinessException("评论不存在！");
        }
        if(!Objects.equals(comment.getUserId(), user.getUserId()) && user.getUserStatus()!=1) {
            throw new MyBusinessException("无权限操作！");
        }
    }
}

