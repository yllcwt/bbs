package com.example.bbs.mapper;

import com.example.bbs.dto.PostDTO;
import com.example.bbs.entity.Post;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yl
 * @since 2021-02-05
 */
@Repository
public interface PostMapper extends BaseMapper<Post> {


    List<PostDTO> selectPost(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

//    Post addPost(@Param("post") Post post);
}
