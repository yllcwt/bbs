package com.example.bbs.controller;


import com.example.bbs.entity.User;
import com.example.bbs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/bbs/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("test")
    public String testUser(){
        return "test";
    }


}

