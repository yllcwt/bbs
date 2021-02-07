package com.example.bbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.bbs.entity.Post;
import com.example.bbs.entity.Tag;
import com.example.bbs.entity.TagPostRef;
import com.example.bbs.mapper.TagPostRefMapper;
import com.example.bbs.service.TagPostRefService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yl
 * @since 2021-02-05
 */
@Service
public class TagPostRefServiceImpl extends ServiceImpl<TagPostRefMapper, TagPostRef> implements TagPostRefService {

    @Autowired TagPostRefMapper tagPostRefMapper;

    @Override
    public void addOrSelectTagPostRef(List<Tag> list, Integer postId) {
        for(int i=0; i < list.size(); i++){
//            TagPostRef tagPostRef = new TagPostRef();
//            tagPostRef.setTagId(list.get(i).getId())
            LambdaQueryWrapper<TagPostRef> lambdaQueryWrapper = Wrappers.lambdaQuery();
            lambdaQueryWrapper.eq(TagPostRef::getTagId, list.get(i).getId())
                              .eq(TagPostRef::getPostId, postId);
            TagPostRef tagPostRef = getOne(lambdaQueryWrapper);
            if(tagPostRef == null){
                tagPostRefMapper.insert(new TagPostRef(list.get(i).getId(), postId));
            }
        }
    }
}
