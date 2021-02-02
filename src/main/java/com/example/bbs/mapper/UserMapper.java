package com.example.bbs.mapper;

import com.example.bbs.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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

    User selectByUserNameAndUserPassword(@Param("p") Map<String, String> map);

    User selectByUserName(String userName);
}
