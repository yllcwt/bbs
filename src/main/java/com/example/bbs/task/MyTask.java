package com.example.bbs.task;

import com.example.bbs.service.MailService;
import lombok.SneakyThrows;

public class MyTask implements Runnable {
    private MailService mailService;
    private String email;
    private String newPassword;

    private MyTask() {}

    public MyTask(MailService mailService, String email, String newPassword) {
        this.mailService = mailService;
        this.email = email;
        this.newPassword = newPassword;
    }

    @SneakyThrows
    @Override
    public void run() {
        mailService.sendEmail(email, "重置密码", "你的密码已重置："+newPassword);
    }
}
