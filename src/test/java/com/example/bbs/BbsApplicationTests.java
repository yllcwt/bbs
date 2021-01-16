package com.example.bbs;

import com.example.bbs.entity.User;
import com.example.bbs.mapper.UserMapper;
import com.example.bbs.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class BbsApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;
    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        for(User user:userList) {
            System.out.println(user);
        }
    }

    @Test
    public  void testUsers(){
        List<User> users = userService.listUsers();
        System.err.println(users);
    }
}
