package com.example.bbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.bbs.dto.PaginationDTO;
import com.example.bbs.entity.Tag;
import com.example.bbs.mapper.TagMapper;
import com.example.bbs.service.TagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yl
 * @since 2021-02-07
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Override
    public List<Tag> addOrSelectTag(String tags) {
        String[] strTags = tags.split(",");
        List<Tag> listTags = new ArrayList<>();
        for(String tag : strTags) {
            Tag originTag = findTagByName(tag);
            Tag newTag = null;
            if(originTag != null){
                listTags.add(originTag);
            }else {
                newTag = new Tag();
                newTag.setTagExplanation(tag);
                tagMapper.insert(newTag);
                listTags.add(newTag);
            }
        }
        return listTags;
    }

    @Override
    public PaginationDTO listTag(Integer pageIndex, Integer pageSize) {
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = tagMapper.selectCount(new QueryWrapper<>());
        Integer offset = paginationDTO.mySetPagination(pageIndex,pageSize,totalCount);
        List<Tag> tagList = tagMapper.listTag(offset, pageSize);
        paginationDTO.setData(tagList);
        return paginationDTO;
    }

    private Tag findTagByName(String tag) {
        QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
        tagQueryWrapper.eq("tag_explanation", tag);
        return getOne(tagQueryWrapper);
    }

    public Tag insert(Tag tag) {
        Tag exitTag = findTagByName(tag.getTagExplanation());
        if(exitTag != null) {
            return exitTag;
        }
        tagMapper.insert(tag);
        return tag;
    }
}
