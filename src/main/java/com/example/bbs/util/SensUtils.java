package com.example.bbs.util;

import com.example.bbs.entity.User;
import io.github.biezhi.ome.OhMyEmail;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Properties;

/**
 * <pre>
 *     常用工具
 * </pre>
 */
@Slf4j
public class SensUtils {

    /**
     * 配置邮件
     *
     * @param smtpHost smtpHost
     * @param userName 邮件地址
     * @param password password
     */
    public static void configMail(String smtpHost, String userName, String password) {
        Properties properties = OhMyEmail.defaultConfig(false);
        properties.setProperty("mail.smtp.host", smtpHost);
        OhMyEmail.config(properties, userName, password);
    }
    public static String HumpToUnderline(String para){
        StringBuilder sb=new StringBuilder(para);
        int temp=0;//定位
        if (!para.contains("_")) {
            for(int i=0;i<para.length();i++){
                if(Character.isUpperCase(para.charAt(i))){
                    sb.insert(i+temp, "_");
                    temp+=1;
                }
            }
        }
        return sb.toString().toUpperCase();
    }
    public static boolean isAdmin(HttpServletRequest request) {
        User user = (User)request.getSession().getAttribute("user");
        System.err.println(user.getUserStatus());
        if(user.getUserStatus() == 0) {
            return false;
        }
        return true;
    }

}
