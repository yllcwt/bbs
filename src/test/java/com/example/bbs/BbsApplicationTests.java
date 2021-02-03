package com.example.bbs;

import com.example.bbs.entity.User;
import com.example.bbs.mapper.UserMapper;
import com.example.bbs.service.UserService;
import io.github.biezhi.ome.OhMyEmail;
import io.github.biezhi.ome.SendMailException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static io.github.biezhi.ome.OhMyEmail.SMTP_QQ;

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
    @Test
    public void testEmail() throws SendMailException {

        OhMyEmail.config(SMTP_QQ(false), "2724059421@qq.com", "joejlbqanbbzdfec");
        OhMyEmail.subject("这是一封测试TEXT邮件")
                .from("yl")
                .to("980775536@qq.com")
                .text("信件内容")
                .send();
    }

}
