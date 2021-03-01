package com.example.bbs.mapper;

import com.example.bbs.dto.PostQueryCondition;
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
@Repository
public interface UserMapper extends BaseMapper<User> {

    List<User> listUsers();

    User selectByUserNameAndUserPassword(@Param("p") Map<String, String> map);

    User selectByUserName(String userName);

    void updateUserPasswordByUserId(Map<String, String> map);

    Integer calculateAmount(@Param("postQueryCondition") PostQueryCondition postQueryCondition);

    List<User> listUser(PostQueryCondition postQueryCondition, Integer offset, Integer pageSize);
}
