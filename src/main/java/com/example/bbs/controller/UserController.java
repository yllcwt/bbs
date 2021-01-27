package com.example.bbs.controller;


import com.example.bbs.dto.JsonResult;
import com.example.bbs.entity.User;
import com.example.bbs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    @GetMapping("test")
    public String testUser(){
        return "test";
    }

    @GetMapping("testUser")
    @ResponseBody
    public void test(){
        System.out.println( userService.listUsers());
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
        System.out.println(userName+userPassword+reUserPassword+userAge+userSex+userEmail);
        return JsonResult.success("success");
    }


}

