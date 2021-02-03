package com.example.bbs.controller;


import com.example.bbs.dto.JsonResult;
import com.example.bbs.entity.User;
import com.example.bbs.service.MailService;
import com.example.bbs.service.UserService;
import com.example.bbs.util.RegexUtil;
import io.github.biezhi.ome.SendMailException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
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

    @GetMapping("test")
    public String testUser(){
        return "test";
    }

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
                                   @RequestParam String userEmail){
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


}

