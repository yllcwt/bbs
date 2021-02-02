package com.example.bbs.service;

import com.example.bbs.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yl
 * @since 2021-01-16
 */
public interface UserService extends IService<User> {

    List<User> listUsers();

    User checkLogin(String userName, String userPassword);

    User selectByUserName(String userName);
}
