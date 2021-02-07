package com.example.bbs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class MyConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry){
        //添加视图映射
//        registry.addViewController("/test").setViewName("test");

    }

    @Bean
    public WebMvcConfigurer webMvcConfigurer(){
        WebMvcConfigurer webMvcConfigurer = new WebMvcConfigurer() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry){
                registry.addViewController("/register").setViewName("registration");
                registry.addViewController("/main").setViewName("index");
                registry.addViewController("/post_publish").setViewName("post_publish");
                registry.addViewController("/login").setViewName("login");
                registry.addViewController("/menu").setViewName("menu_manage");
                registry.addViewController("/user_manage").setViewName("user_manage");//后台管理
                registry.addViewController("/post_list").setViewName("post_list");//文章列表
                registry.addViewController("/post_tag").setViewName("post_tag");//
                registry.addViewController("/post_category").setViewName("post_category");//
                registry.addViewController("/my_index").setViewName("my_index");//
            }
        };
        return webMvcConfigurer;
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:///" + System.getProperties().getProperty("user.home") + "/sens/upload/");
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}