package com.example.bbs.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.bbs.entity.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yl
 * @since 2021-02-07
 */
@Controller
public class TagController {

    @GetMapping("/post_tag")
    public String tagList(Model model){
        QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();

    }
}

