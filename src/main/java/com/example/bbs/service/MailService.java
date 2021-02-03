package com.example.bbs.service;

import io.github.biezhi.ome.SendMailException;
import org.springframework.stereotype.Service;

public interface MailService {
    void sendEmail(String email, String title, String content) throws SendMailException;
}
