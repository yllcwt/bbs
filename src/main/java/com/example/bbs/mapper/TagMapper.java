package com.example.bbs.mapper;

import com.example.bbs.entity.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yl
 * @since 2021-02-07
 */
@Repository
public interface TagMapper extends BaseMapper<Tag> {

    List<Tag> findByPostId(Integer postId);
}
