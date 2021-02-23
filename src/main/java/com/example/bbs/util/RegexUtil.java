package com.example.bbs.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 言曌
 * @date 2020/3/8 1:55 下午
 */

public class RegexUtil {

    /**
     * 判断Email合法性
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (email == null) {
            return false;
        }
        String rule = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";
        Pattern pattern = Pattern.compile(rule);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * 密码由字母数字下划线组成 8到16位
     * @param password
     * @return
     */
    public static boolean isPassword(String password) {
        if (password==null) {
            return false;
        }
        String rule = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";
        Pattern pattern = Pattern.compile(rule);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
