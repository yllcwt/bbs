package com.example.bbs.service.impl;

import com.example.bbs.service.MailService;
import com.example.bbs.util.SensUtils;
import io.github.biezhi.ome.OhMyEmail;
import io.github.biezhi.ome.SendMailException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class MailServiceImp implements MailService {
    @Value("${mail.smtp.host}")
    private String host;

    @Value("${mail.smtp.username}")
    private String username;

    @Value("${mail.smtp.password}")
    private String password;

    @Value("${mail.from.name}")
    private String fromName;

    /**
     * 发送邮件
     *
     * @param email      email 接收者
     * @param title   subject 标题
     * @param content content 内容
     */
    @Override
    public void sendEmail(String email, String title, String content) throws SendMailException{
        //配置邮件服务器
        SensUtils.configMail(host, username, password);
        OhMyEmail.subject(title)
                .from(fromName)
                .to(email)
                .text(content)
                .send();
    }
}
