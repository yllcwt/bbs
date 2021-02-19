package com.example.bbs.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.bbs.dto.PaginationDTO;
import com.example.bbs.dto.PostQueryCondition;
import com.example.bbs.entity.Category;
import com.example.bbs.service.CategoryService;
import com.example.bbs.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private PostService postService;

    @GetMapping("/post_category")
    public String categoryList(Model model){
        List<Category> categoryList = categoryService.list();
        model.addAttribute("categoryList", categoryList);
        return "post_category";
    }

    @GetMapping("/category")
    public String searchCategory(Model model,
                                 @RequestParam(value = "keywords", required = false) String keywords){
        QueryWrapper<Category> categoryQueryWrapper = new QueryWrapper<>();
        categoryQueryWrapper.like("category_name", keywords).or().like("category_explanation", keywords);
        List<Category> categoryList = categoryService.list(categoryQueryWrapper);
        model.addAttribute("categoryList", categoryList);
        return "post_category";
    }
//    @GetMapping("/category/{id}")
//    public String categoryPost(@PathVariable("id") Integer id,
//                               Model model){
//        PostQueryCondition postQueryCondition = new PostQueryCondition();
//        postQueryCondition.setCategoryId(id);
//        PaginationDTO paginationDTO = postService.listPost(1, 10, postQueryCondition);
//        model.addAttribute("paginationDTO", paginationDTO);
//        model.addAttribute("category", id);
//        return "redirect:/homepage";
//    }
}
