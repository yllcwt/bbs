package com.example.bbs.controller;

import com.example.bbs.dto.PaginationDTO;
import com.example.bbs.entity.Post;
import com.example.bbs.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private PostService postService;

    @GetMapping("/homepage")
    public String index(Model model,
                            @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                            @RequestParam(value = "search", required = false) String search,
                            @RequestParam(value = "tag", required = false) String tag,
                            @RequestParam(value = "category", required = false) String category) {
        PaginationDTO paginationDTO = postService.listPost(pageIndex, pageSize, search, tag, category);
        model.addAttribute("paginationDTO", paginationDTO);
        model.addAttribute("search", search);
        model.addAttribute("tag", tag);
        model.addAttribute("category", category);
        return "my_index";
    }

}
