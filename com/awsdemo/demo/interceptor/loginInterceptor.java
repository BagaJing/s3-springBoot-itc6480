package com.awsdemo.demo.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class loginInterceptor extends HandlerInterceptorAdapter {
    private String access_token = null;

    public loginInterceptor(String access_token) {
        this.access_token = access_token;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.addHeader("Authorization","Bearer "+this.access_token);
        return true;
    }
}
