package com.example.bbs.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.bbs.dto.JsonResult;
import com.example.bbs.dto.PaginationDTO;
import com.example.bbs.dto.PostDTO;
import com.example.bbs.dto.PostQueryCondition;
import com.example.bbs.entity.*;
import com.example.bbs.exception.MyBusinessException;
import com.example.bbs.service.*;
import com.example.bbs.util.CommentUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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
    @Autowired
    private UserLikeService userLikeService;
    @Autowired
    private CommentLikeService commentLikeService;

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

//        Category category = new Category();
//        category.setId(categoryId);
//
//        PostDTO postDTO = new PostDTO();
//        BeanUtil.copyProperties(post, postDTO);
//
//        if(StringUtils.isNotEmpty(tags)){
//            List<Tag> list = tagService.addOrSelectTag(StringUtils.deleteWhitespace(tags));
//            postDTO.setTagList(list);
//        }
//        postDTO.setCategory(category);
//        postService.insertOrUpdatePost(post);
        if(post.getPostId() == null) {

            postService.save(post); //?
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
        } else {
            postService.updateById(post);
            LambdaQueryWrapper<CategoryPostRef> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(CategoryPostRef::getPostId, post.getPostId());
            categoryPostRefService.remove(wrapper);

            //分类
            CategoryPostRef categoryPostRef = new CategoryPostRef();
            //这里没有new 进行赋值
            categoryPostRef.setCategoryId(categoryId);
            categoryPostRef.setPostId(post.getPostId());
            categoryPostRefService.addOrSelectCategoryPostRef(categoryPostRef);

            LambdaQueryWrapper<TagPostRef> wrapper1 = Wrappers.lambdaQuery();
            wrapper1.eq(TagPostRef::getPostId, post.getPostId());
            tagPostRefService.remove(wrapper1);

            if(StringUtils.isNotEmpty(tags)){
                List<Tag> list = tagService.addOrSelectTag(StringUtils.deleteWhitespace(tags));
                tagPostRefService.addOrSelectTagPostRef(list,post.getPostId());
            }
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
                           Model model,
                           HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");

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

        if(user != null){
            LambdaQueryWrapper<UserLike> userLikeLambdaQueryWrapper = Wrappers.lambdaQuery();
            userLikeLambdaQueryWrapper.eq(UserLike::getUserId, user.getUserId());
            userLikeLambdaQueryWrapper.eq(UserLike::getPostId, postId);
            UserLike userLike = userLikeService.getOne(userLikeLambdaQueryWrapper);
            if(userLike != null) {
                model.addAttribute("userLike", userLike);
            }

            for (Comment comment : commentList) {
                LambdaQueryWrapper<CommentLike> commentLikeLambdaQueryWrapper = Wrappers.lambdaQuery();
                commentLikeLambdaQueryWrapper.eq(CommentLike::getCommentId, comment.getCommentId())
                                             .eq(CommentLike::getUserId, user.getUserId());
                CommentLike commentLike = commentLikeService.getOne(commentLikeLambdaQueryWrapper);
                if(commentLike == null) {
                    comment.setUserLikeComment(false);
                } else {
                    comment.setUserLikeComment(true);
                }
            }
        }

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
        model.addAttribute("paginationDTO", paginationDTO);
        return "post_list";
    }

    @GetMapping("/postBatchDelete")
    @ResponseBody
    public JsonResult postBatchDelete(@RequestParam("ids") List<Integer> ids, HttpServletRequest request){

        if (ids == null || ids.size() == 0 || ids.size() > 10) {
            return JsonResult.error("参数不合法!");
        }

        List<Post> postList =postService.listByIds(ids);
        for (Post post : postList) {
            basicCheck(post, request);
        }

        postService.removeByIds(ids);
        return JsonResult.success("删除成功！");
    }

    @PostMapping("/postStickIt")
    @ResponseBody
    public JsonResult postStickIt(@RequestParam("postId") Integer postId, HttpServletRequest request) {
        Post post = postService.getById(postId);
        basicCheck(post, request);
        post.setPostTop(1);
        postService.updateById(post);
        return JsonResult.success("置顶成功！");
    }
    @PostMapping("/postUnStickIt")
    @ResponseBody
    public JsonResult postUnStickIt(@RequestParam("postId") Integer postId, HttpServletRequest request) {
        Post post = postService.getById(postId);
        basicCheck(post, request);
        post.setPostTop(0);
        postService.updateById(post);
        return JsonResult.success("取消置顶成功！");
    }
    @PostMapping("/postThrowIt")
    @ResponseBody
    public JsonResult postThrowIt(@RequestParam("postId") Integer postId, HttpServletRequest request) {
        Post post = postService.getById(postId);
        basicCheck(post, request);
        postService.removeById(post.getPostId());
        return JsonResult.success("删除成功！");
    }
    @GetMapping("/postEdit")
    public String postEdit(Model model,
                           @RequestParam("postId") Integer postId,
                           HttpServletRequest request) {
        PostDTO postDTO = postService.findPostByPostId(postId);
//        basicCheck(postDTO, request); 完成项目后加上去

        List<String> strList = new ArrayList<>();
        for (Tag tag : postDTO.getTagList()) {
            strList.add(tag.getTagExplanation());
        }
        String[] str = strList.toArray(new String[strList.size()]);
        String strings = StringUtils.join(str, ",");

        System.err.println(postDTO.getCategory().getId());
        List<Category> categoryList = categoryService.list();
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("strings", strings);
        model.addAttribute("postDTO", postDTO);
        return "post_edit";
    }
    @PostMapping("/postLike")
    @ResponseBody
    public JsonResult postLike(@RequestParam("postId") Integer postId,
                               @RequestParam("postLike") Integer postLike,
                               HttpServletRequest request) {
        //to do 判断是否登录
        User user = (User) request.getSession().getAttribute("user");
        LambdaQueryWrapper<UserLike> userLikeLambdaQueryWrapper = Wrappers.lambdaQuery();
        userLikeLambdaQueryWrapper.eq(UserLike::getUserId, user.getUserId());
        userLikeLambdaQueryWrapper.eq(UserLike::getPostId, postId);
        UserLike userLike = userLikeService.getOne(userLikeLambdaQueryWrapper);
        if(userLike == null) {
            UserLike newUserLike = new UserLike();
            newUserLike.setPostId(postId);
            newUserLike.setUserId(user.getUserId());
            userLikeService.save(newUserLike);

            Post post = postService.getById(postId);
            post.setPostLike(postLike+1);
            postService.updateById(post);
            return JsonResult.success("点赞成功！");
        } else {
            userLikeService.remove(userLikeLambdaQueryWrapper);
            Post post = postService.getById(postId);
            post.setPostLike(postLike-1);
            postService.updateById(post);
            return JsonResult.error("取消点赞成功！");
        }

    }

    private void basicCheck(Post post, HttpServletRequest request) {
        User user = (User)request.getSession().getAttribute("user");

        if(user == null) {
            throw new MyBusinessException("请先登录！");
        }
        if(post == null) {
            throw new MyBusinessException("文章不存在！");
        }
        if(!Objects.equals(post.getUserId(), user.getUserId()) && user.getUserStatus()!=1) {
            throw new MyBusinessException("无权限操作！");
        }
    }
}
