package com.example.bbs.service.impl;

import com.example.bbs.entity.User;
import com.example.bbs.mapper.UserMapper;
import com.example.bbs.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
