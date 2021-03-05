package com.example.bbs.config;

import com.example.bbs.interceptor.AdminInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;


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
//                registry.addViewController("/login").setViewName("login");
                registry.addViewController("/menu").setViewName("menu_manage");
                registry.addViewController("/post_list").setViewName("post_list");//文章列表

//                registry.addViewController("/user_list").setViewName("user_list");
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
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册TestInterceptor拦截器
        InterceptorRegistration registration = registry.addInterceptor(new AdminInterceptor());
        registration.addPathPatterns("/**");                      //所有路径都被拦截
        registration.excludePathPatterns(                         //添加不拦截路径
                "/login",            //登录
                "**.html",            //html静态资源
                "**.js",              //js静态资源
                "**.css",             //css静态资源
                "**.woff",
                "**.ttf",
                "/static/*",
                "/homepage",
                "/post/**",
                "/postCategory",
                "/postTag",
                "/register",
                "**.jpg"
        );
    }
}