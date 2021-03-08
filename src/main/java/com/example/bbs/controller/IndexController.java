package com.example.bbs.controller;

import com.example.bbs.dto.PaginationDTO;
import com.example.bbs.dto.PostQueryCondition;
import com.example.bbs.entity.Post;
import com.example.bbs.entity.User;
import com.example.bbs.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private PostService postService;

    @GetMapping("/homepage")
    public String index(Model model,
                        @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                        @RequestParam(value = "keywords", required = false) String keywords,
                        @RequestParam(value = "tag", required = false) Integer tag,
                        @RequestParam(value = "category", required = false) Integer category,
                        @RequestParam(value = "buttonType", defaultValue = "new") String buttonType,
                        HttpServletRequest request) {
        PostQueryCondition postQueryCondition = new PostQueryCondition();
        User user = (User)request.getSession().getAttribute("user");
        if(buttonType.equals("my")) {
            if(user == null) {
                return "redirect:/main";
            }
            postQueryCondition.setUserId(user.getUserId());
        }
        postQueryCondition.setKeywords(keywords);
        postQueryCondition.setCategoryId(category);
        postQueryCondition.setTagId(tag);
        postQueryCondition.setButtonType(buttonType.equals("new") ? 1 : buttonType.equals("hot") ? 2 : 3);
        PaginationDTO paginationDTO = postService.listPost(pageIndex, pageSize, postQueryCondition);
        model.addAttribute("paginationDTO", paginationDTO);
        model.addAttribute("keywords", keywords);
        model.addAttribute("tag", tag);
        model.addAttribute("category", category);
        model.addAttribute("buttonType", buttonType);
        return "my_index";
    }

}
