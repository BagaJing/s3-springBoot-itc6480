package com.awsdemo.demo.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class webConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new loginInterceptor())
                .addPathPatterns("/dev/**")
                .excludePathPatterns("/dev/login")
                .excludePathPatterns("/dev/register");


        /*
        registry.addInterceptor(new loginInterceptor())
                .addPathPatterns("/depoytest3/client")
                .excludePathPatterns("/depoytest3/client/login")
                .excludePathPatterns("/depoytest3/register");

         */
    }
}
