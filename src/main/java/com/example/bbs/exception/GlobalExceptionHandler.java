package com.example.bbs.exception;

import com.example.bbs.dto.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MyBusinessException.class)
    @ResponseBody
    public JsonResult processApiException(HttpServletResponse response,
                                          MyBusinessException e) {
        JsonResult result = new JsonResult(0, e.getMessage());
        response.setStatus(200);
        response.setContentType("application/json;charset=UTF-8");
        log.error("业务异常，提示前端操作不合法,{},{}", e.getMessage(), e);
        return result;
    }

    /**
     * 是否是ajax请求
     */
    public static boolean isAjax(HttpServletRequest httpRequest) {
        return (httpRequest.getHeader("X-Requested-With") != null
                && "XMLHttpRequest"
                .equals(httpRequest.getHeader("X-Requested-With").toString()));
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest request,
                                            HttpServletResponse response,
                                            Exception e, Model model) throws IOException {
        e.printStackTrace();

        if (isAjax(request)) {
            ModelAndView mav = new ModelAndView();
            MappingJackson2JsonView view = new MappingJackson2JsonView();
            Map<String, Object> attributes = new HashMap<String, Object>();
//            if (e instanceof UnauthorizedException) {
//                attributes.put("msg", "没有权限");
//            } else {
                attributes.put("msg", e.getMessage());
//            }
            attributes.put("code", "0");
            view.setAttributesMap(attributes);
            mav.setView(view);
            return mav;
        }

//        if (e instanceof UnauthorizedException) {
//            //请登录
//            log.error("无权访问", e);
//            return new ModelAndView("common/error/403");
//        }
        //其他异常
        String message = e.getMessage();
        System.err.println("Exception");
//        model.addAttribute("code", 500);
//        model.addAttribute("message", message);
        return new ModelAndView("error/500");
    }
}
