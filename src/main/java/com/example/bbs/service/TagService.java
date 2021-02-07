package com.example.bbs.service;

import com.example.bbs.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yl
 * @since 2021-02-07
 */
public interface TagService extends IService<Tag> {

    List<Tag> addOrSelectTag(String tags);
}
