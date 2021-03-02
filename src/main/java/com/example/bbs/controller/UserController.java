package com.example.bbs.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.bbs.dto.JsonResult;
import com.example.bbs.dto.PaginationDTO;
import com.example.bbs.dto.PostQueryCondition;
import com.example.bbs.entity.*;
import com.example.bbs.service.*;
import com.example.bbs.util.RegexUtil;
import com.example.bbs.util.SensUtils;
import io.github.biezhi.ome.SendMailException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

import static java.lang.System.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yl
 * @since 2021-01-16
 */
@Controller
//@RequestMapping("/bbs/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private MailService mailService;
    @Autowired
    private PostService postService;
    @Autowired
    private CategoryPostRefService categoryPostRefService;
    @Autowired
    private TagPostRefService tagPostRefService;
    @Autowired
    private CommentService commentService;

    @GetMapping("testUser")
    @ResponseBody
    public void test(){
        out.println( userService.listUsers());
    }

    @PostMapping("userLogin")
    @ResponseBody
    public JsonResult userLogin(@RequestParam String userName,
                                @RequestParam String userPassword,
                                HttpServletRequest request) {
        User user = userService.checkLogin(userName, userPassword);
        if (user == null) {
            return JsonResult.error("登录失败");
        } else {
            request.getSession().setAttribute("user", user);
            return JsonResult.success("登录成功");
        }

    }

    @PostMapping("userRegister")
    @ResponseBody
    public JsonResult userRegister(@RequestParam String userName,
                                   @RequestParam String userPassword,
                                   @RequestParam String reUserPassword,
                                   @RequestParam String userSex,
                                   @RequestParam String userAge,
                                   @RequestParam String userEmail,
                                   HttpServletRequest request){
        String basePath = request.getScheme()+"://"+request.getServerName()+":"+
                request.getServerPort()+request.getContextPath();
        System.err.println(basePath);
        //判断合法性
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(userPassword) || StringUtils.isEmpty(reUserPassword)
                || StringUtils.isEmpty(userSex) || StringUtils.isEmpty(userAge) || StringUtils.isEmpty(userEmail)) {
            return JsonResult.error("请输入完整信息！");
        }
        if (!RegexUtil.isEmail(userEmail)){
            return JsonResult.error("邮箱格式不正确！");
        }
        if (userPassword.length() > 20 || userPassword.length() < 3) {
            return JsonResult.error("密码长度不正确！");
        }
        if (!userPassword.equals(reUserPassword)) {
            return JsonResult.error("输入密码不一致！");
        }
        //判断用户名是否被注册
        User user = userService.selectByUserName(userName);
        if(user != null){
            return JsonResult.error("用户名已存在！");
        }else{
            //插入
            user = new User();
            user.setUserName(userName);
            user.setUserAge(Integer.parseInt(userAge));
            user.setUserSex(Integer.parseInt(userSex));
            user.setUserEmail(userEmail);
            user.setUserStatus(0);
            user.setUserPassword(userPassword);
            user.setUserImage(basePath+"/static/images/avatar/" + RandomUtils.nextInt(1, 41) + ".jpeg");
            user.setUserCreateTime(new Date());
            user.setUserDisplayName(userName);
            boolean flag = false;
            flag = userService.save(user);
            if (flag) {
                return JsonResult.success("注册成功！");
            } else {
                return JsonResult.error("注册失败！");
            }
        }

    }
    @PostMapping("userForget")
    @ResponseBody
    public JsonResult userForget(@RequestParam("email") String email, @RequestParam("forgetUserName") String forgetUserName){
        User user = userService.selectByUserName(forgetUserName);
        if(user != null && user.getUserEmail().equalsIgnoreCase(email)){
            //验证成功，将新密码邮件发送给他
            String newPassword = RandomStringUtils.randomNumeric(8);
            userService.updateUserPasswordByUserId(user.getUserId(),newPassword);
            //send email
            try {
                mailService.sendEmail(email, "重置密码", "你的密码已重置："+newPassword);
            }catch (SendMailException e){
                e.printStackTrace();
                return JsonResult.error("邮箱发送失败，系统配置smtp失败");
            }
        }else {
            return JsonResult.error("用户名与邮箱账号不一致！");
        }
        return JsonResult.success("密码已发送到你的邮箱！");
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        if(request.getSession().getAttribute("user")!=null)
        {
            request.getSession().removeAttribute("user");
        }
        return "redirect:/login";
    }

    @PostMapping("/userUpdateInfo")
    @ResponseBody
    public JsonResult userUpdateInfo (@RequestParam(value = "userImage",required = false) String userImage,
                                      @RequestParam("userName") String userName,
                                      @RequestParam("userDisplayName") String userDisplayName,
                                      @RequestParam("userEmail") String userEmail,
                                      @RequestParam(value = "userInterest",required = false) String userInterest,
                                      @RequestParam(value = "userId", required = false) Integer userId,
                                      HttpServletRequest request) {
        User user = null;
        if(userId == null) {
            user = (User)request.getSession().getAttribute("user");
            if(user == null){
                return JsonResult.error("请先登录！");
            }
        } else {
            user = userService.getById(userId);
        }

        if(!RegexUtil.isEmail(userEmail)) {
            return JsonResult.error("邮箱格式错误！");
        }
        if(userService.selectByUserName(userName)!=null && !user.getUserName().equals(userName)) {
            return JsonResult.error("用户名已存在！");
        }
        if(StringUtils.isNotEmpty(userImage)) {
            user.setUserImage(userImage);
        }
        user.setUserDisplayName(userDisplayName);
        user.setUserName(userName);
        user.setUserEmail(userEmail);
        user.setUserInterest(userInterest);
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getUserId, user.getUserId());
        if(userService.update(user, wrapper) && userId == null) {
            request.getSession().setAttribute("user", user);
            return JsonResult.success("更新成功！");
        } else if(userService.update(user, wrapper) && userId != null){
            return JsonResult.success("更新成功！");
        }
        return JsonResult.error("更新失败！");
    }

    @PostMapping("/userUpdatePassword")
    @ResponseBody
    public JsonResult userUpdatePassword(@RequestParam("beforePassword") String beforePassword,
                                         @RequestParam("newPassword") String newPassword,
                                         HttpServletRequest request) {
        User user = (User)request.getSession().getAttribute("user");
        if(user == null){
            return JsonResult.error("请先登录！");
        }
        if(!user.getUserPassword().equals(beforePassword)) {
            return JsonResult.error("旧密码错误！");
        }
        if(!RegexUtil.isPassword(newPassword)){
            if(newPassword.length() < 6 || newPassword.length()>16)
            {
                return JsonResult.error("密码长度不够！");
            }
            return JsonResult.error("密码必须由字母和数字组成！");
        }
        user.setUserPassword(newPassword);
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getUserId, user.getUserId());
        if(userService.update(user, wrapper)) {
            request.getSession().setAttribute("user", user);
            return JsonResult.success("密码更新成功！");
        }
        return JsonResult.error("密码更新失败！");
    }
    @GetMapping("/userList")
    public String userList(Model model,
                           @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                           @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                           @RequestParam(value = "sort",defaultValue = "none") String userPower,
                           @RequestParam(value = "searchType",defaultValue = "userDisplayName") String searchType,
                           @RequestParam(value = "keywords", defaultValue = "")String keywords) {
        PostQueryCondition postQueryCondition = new PostQueryCondition();
        postQueryCondition.setSort(userPower.equals("none") ? "none": userPower.equals("admin") ? "1" : "0");
//        postQueryCondition.setOrder(order);
        postQueryCondition.setKeywords(keywords);
        postQueryCondition.setSearchType(SensUtils.HumpToUnderline(searchType));
        PaginationDTO paginationDTO = userService.listUser(pageIndex,pageSize,postQueryCondition);

        model.addAttribute("paginationDTO", paginationDTO);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("sort", userPower);
        model.addAttribute("searchType", searchType);
        model.addAttribute("keywords", keywords);
        return "user_list";
    }
    @PostMapping("/userDelete")
    @ResponseBody
    public JsonResult userDelete(@RequestParam("userId") Integer userId,
                                 HttpServletRequest request) {
        deleteUserRelation(userId, request);
        userService.removeById(userId);
        return JsonResult.success("删除成功！");
    }
    //需要做拦截
    @PostMapping("/userBatchDelete")
    @ResponseBody
    public JsonResult userBatchDelete(@RequestParam("ids") List<Integer> ids,
                                      HttpServletRequest request) {
        if (ids == null || ids.size() == 0 || ids.size() >= 10) {
            return JsonResult.error("参数不合法!");
        }
        List<User> userList = userService.listByIds(ids);
        for (User user : userList) {
            deleteUserRelation(user.getUserId(), request);
            userService.removeById(user.getUserId());
        }
        return JsonResult.success("删除成功！");
    }

    @GetMapping("/userEdit")
    public String userEdit(@RequestParam(value = "userId", required = false) Integer userId,
                           HttpServletRequest request,
                           Model model) {
        if(userId == null) {
            User user = (User)request.getSession().getAttribute("user");
            userId = user.getUserId();
        }
        User userInfo = userService.getById(userId);
        model.addAttribute("userInfo", userInfo);
        return "user_edit";
    }
    @GetMapping("/userInformation")
    public String userInformation(HttpServletRequest request,
                                  Model model) {
        User user = (User)request.getSession().getAttribute("user");
        model.addAttribute("user", user);
        return "user_manage";
    }
    private void deleteUserRelation(Integer userId, HttpServletRequest request) {
        User user = (User)request.getSession().getAttribute("user");
        //删除该用户发表文章
        LambdaQueryWrapper<Post> postLambdaQueryWrapper = Wrappers.lambdaQuery();
        postLambdaQueryWrapper.eq(Post::getUserId, userId);
        List<Post> postList = postService.list(postLambdaQueryWrapper);
        for (Post post : postList) {
            //删除文章分类关联关系
            LambdaQueryWrapper<CategoryPostRef> categoryPostRefLambdaQueryWrapper = Wrappers.lambdaQuery();
            categoryPostRefLambdaQueryWrapper.eq(CategoryPostRef::getPostId, post.getPostId());
            categoryPostRefService.remove(categoryPostRefLambdaQueryWrapper);
            //删除文章标签关联关系
            LambdaQueryWrapper<TagPostRef> tagPostRefLambdaQueryWrapper = Wrappers.lambdaQuery();
            tagPostRefLambdaQueryWrapper.eq(TagPostRef::getPostId, post.getPostId());
            List<TagPostRef> tagPostRefList = tagPostRefService.list(tagPostRefLambdaQueryWrapper);
            for (TagPostRef tagPostRef : tagPostRefList) {
                tagPostRefService.removeById(tagPostRef.getId());
            }
        }
        for (Post post : postList) {
            postService.removeById(post.getPostId());
        }
        //删除用户发表的评论
        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = Wrappers.lambdaQuery();
        commentLambdaQueryWrapper.eq(Comment::getUserId, userId);
        commentService.remove(commentLambdaQueryWrapper);

        if(user.getUserId() == userId) {
            request.getSession().invalidate();
        }
    }
}

