package com.example.bbs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@Component
public class MyConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry){
        //添加视图映射
        registry.addViewController("/test").setViewName("test");

    }

    @Bean
    public WebMvcConfigurer webMvcConfigurer(){
        WebMvcConfigurer webMvcConfigurer = new WebMvcConfigurer() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry){
                registry.addViewController("/register").setViewName("registration");
                registry.addViewController("/main").setViewName("index");
                registry.addViewController("/publish.html").setViewName("publish");
                registry.addViewController("/login").setViewName("login");
                registry.addViewController("/menu").setViewName("menu_manage");
            }
        };
        return webMvcConfigurer;
    }
}