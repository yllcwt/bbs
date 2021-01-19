package com.example.bbs.mapper;

import com.example.bbs.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yl
 * @since 2021-01-16
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    List<User> listUsers();
}
