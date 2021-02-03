package com.example.bbs.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.example.bbs.entity.User;
import com.example.bbs.mapper.UserMapper;
import com.example.bbs.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yl
 * @since 2021-01-16
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> listUsers() {
        return userMapper.listUsers();
    }

    @Override
    public User checkLogin(String userName, String userPassword) {
        Map<String, String> map = new HashMap<>();
        map.put("userName", userName);
        map.put("userPassword", userPassword);
        User user = userMapper.selectByUserNameAndUserPassword(map);
        return user;
    }

    @Override
    public User selectByUserName(String userName) {
        return userMapper.selectByUserName(userName);
    }

    @Override
    public void updateUserPasswordByUserId(Integer userId, String newPassword) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", Integer.toString(userId));
        map.put("newPassword", newPassword);
        userMapper.updateUserPasswordByUserId(map);
    }
}
