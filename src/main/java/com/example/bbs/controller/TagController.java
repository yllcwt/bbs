package com.example.bbs.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.bbs.dto.JsonResult;
import com.example.bbs.dto.PaginationDTO;
import com.example.bbs.dto.TagDTO;
import com.example.bbs.entity.Tag;
import com.example.bbs.entity.TagPostRef;
import com.example.bbs.service.TagPostRefService;
import com.example.bbs.service.TagService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;


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

    @Autowired
    private TagService tagService;
    @Autowired
    private TagPostRefService tagPostRefService;

    @GetMapping("/post_tag")
    public String tagList(Model model){

        List<Tag> tagList = tagService.list();
        model.addAttribute("tagDTOList", search(tagList));
        return "post_tag";
    }

    @GetMapping("/tag")
    public String searchTag(Model model,
                            @RequestParam(value = "keywords",defaultValue = "") String keywords ){
        LambdaQueryWrapper<Tag> wrapper = Wrappers.lambdaQuery();
        wrapper.like(Tag::getTagExplanation, keywords);
        List<Tag> tagList = tagService.list(wrapper);

        model.addAttribute("tagDTOList", search(tagList));
        return "post_tag";
    }

    private List<TagDTO> search(List<Tag> tagList){
        List<TagDTO> tagDTOList = new ArrayList<>();

        for(Tag tag : tagList){
            LambdaQueryWrapper<TagPostRef> wrappers = Wrappers.lambdaQuery();
            wrappers.eq(TagPostRef::getTagId, tag.getId());
            Integer tagCount = tagPostRefService.count(wrappers);
            TagDTO tagDTO = new TagDTO();
            BeanUtils.copyProperties(tag, tagDTO);
            tagDTO.setPostCount(tagCount);
            tagDTOList.add(tagDTO);
        }
        return tagDTOList;
    }
    @GetMapping("/postTagManage")
    public String postTagManage(Model model,
                                @RequestParam(value = "pageIndex",defaultValue = "1") Integer pageIndex,
                                @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        PaginationDTO paginationDTO = tagService.listTag(pageIndex, pageSize);
        model.addAttribute("paginationDTO", paginationDTO);
        return "post_tag_manage";
    }
    @GetMapping("/postTagEdit")
    public String postTagEdit(Model model,
                                   @RequestParam("pageIndex") Integer pageIndex,
                                   @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                   @RequestParam("tagId") Integer tagId) {
        Tag targetTag = tagService.getById(tagId);
        PaginationDTO paginationDTO = tagService.listTag(pageIndex, pageSize);
        model.addAttribute("paginationDTO", paginationDTO);
        model.addAttribute("targetTag",targetTag);
        return "post_tag_manage";
    }
    @PostMapping("/postTagSave")
    @ResponseBody
    public JsonResult postCategorySave(@RequestParam("tagExplanation") String tagExplanation,
                                       @RequestParam(value = "tagId", required = false) Integer tagId) {
        Tag tag = new Tag();
        tag.setTagExplanation(tagExplanation);
        if(tagId != null) {
            tag.setId(tagId);
            tagService.updateById(tag);
        } else {
            LambdaQueryWrapper<Tag> categoryLambdaQueryWrapper = Wrappers.lambdaQuery();
            categoryLambdaQueryWrapper.eq(Tag::getTagExplanation, tag.getTagExplanation());
            Tag oldTag = tagService.getOne(categoryLambdaQueryWrapper);
            if(oldTag != null) {
                return JsonResult.error("标签名已存在！");
            }
            tagService.save(tag);
        }
        return JsonResult.success("操作成功！");
    }
    @PostMapping("/postTagDelete")
    @ResponseBody
    public JsonResult postTagDelete(@RequestParam("tagId") Integer tagId) {
        tagService.removeById(tagId);
        return JsonResult.success("删除成功！");
    }
}

