package com.example.bbs.interceptor;

import com.example.bbs.entity.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AdminInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        try {
            //统一拦截（查询当前session是否存在user）(这里user会在每次登陆成功后，写入session)
            User user=(User)request.getSession().getAttribute("user");
            if(user!=null){
                return true;
            }else {
                if (request.getHeader("x-requested-with") != null && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) { //如果是ajax请求响应头会有x-requested-with
                    System.out.println("ajax请求被拦截");
//                    response.getWriter().print("loseSession");
                    response.sendRedirect(request.getContextPath()+"login");
                    return true;
                }
                response.sendRedirect(request.getContextPath()+"login");
//                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
