package com.example.bbs.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.bbs.dto.JsonResult;
import com.example.bbs.dto.PaginationDTO;
import com.example.bbs.dto.PostQueryCondition;
import com.example.bbs.entity.Category;
import com.example.bbs.entity.CategoryPostRef;
import com.example.bbs.service.CategoryPostRefService;
import com.example.bbs.service.CategoryService;
import com.example.bbs.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryPostRefService categoryPostRefService;

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
    @GetMapping("/postCategoryManage")
    public String postCategoryManage(Model model,
                                     @RequestParam(value = "pageIndex",defaultValue = "1") Integer pageIndex,
                                     @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        PaginationDTO paginationDTO = categoryService.listCategory(pageIndex, pageSize);
        model.addAttribute("paginationDTO", paginationDTO);
        return "post_category_manage";
    }
    @GetMapping("/postCategoryEdit")
    public String postCategoryEdit(Model model,
                                   @RequestParam("pageIndex") Integer pageIndex,
                                   @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                   @RequestParam("categoryId") Integer categoryId) {
        Category targetCategory = categoryService.getById(categoryId);
        PaginationDTO paginationDTO = categoryService.listCategory(pageIndex, pageSize);
        model.addAttribute("paginationDTO", paginationDTO);
        model.addAttribute("targetCategory",targetCategory);
        return "post_category_manage";
    }
    @PostMapping("/postCategorySave")
    @ResponseBody
    public JsonResult postCategorySave(@RequestParam("cateName") String categoryName,
                                       @RequestParam("cateDesc") String categoryExplanation,
                                       @RequestParam(value = "categoryId", required = false) Integer categoryId) {
        Category category = new Category();
        category.setCategoryName(categoryName);
        category.setCategoryExplanation(categoryExplanation);
        if(categoryId != null) {
            category.setId(categoryId);
            categoryService.updateById(category);
        } else {
            LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = Wrappers.lambdaQuery();
            categoryLambdaQueryWrapper.eq(Category::getCategoryName, category.getCategoryName());
            Category oldCategory = categoryService.getOne(categoryLambdaQueryWrapper);
            if(oldCategory != null) {
                return JsonResult.error("分类名已存在！");
            }
            categoryService.save(category);
        }
        return JsonResult.success("操作成功！");
    }
    @PostMapping("/postCategoryDelete")
    @ResponseBody
    public JsonResult postCategoryDelete(@RequestParam("categoryId") Integer categoryId) {
        LambdaQueryWrapper<CategoryPostRef> categoryPostRefLambdaQueryWrapper = Wrappers.lambdaQuery();
        categoryPostRefLambdaQueryWrapper.eq(CategoryPostRef::getCategoryId, categoryId);
        List<CategoryPostRef> categoryPostRefList = categoryPostRefService.list(categoryPostRefLambdaQueryWrapper);
        if(categoryPostRefList.size() > 0) {
            return JsonResult.error("该分类已有文章，无法删除！");
        } else {
            categoryService.removeById(categoryId);
        }
        return JsonResult.success("删除成功！");
    }
}
