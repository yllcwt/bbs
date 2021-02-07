package com.example.bbs.service;

import com.example.bbs.entity.Tag;
import com.example.bbs.entity.TagPostRef;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yl
 * @since 2021-02-05
 */
public interface TagPostRefService extends IService<TagPostRef> {

    void addOrSelectTagPostRef(List<Tag> list, Integer postId);
}
