package com.awsdemo.demo.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@WebFilter("/depoytest3/client/*")
public class addHeaderFilter implements Filter {
    private String access_token;

    public addHeaderFilter(String access_token) {
        this.access_token = access_token;
    }

    @Override
    public void destroy() {
    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        httpServletResponse.addHeader("Authorization","Bearer "+this.access_token);
        filterChain.doFilter(servletRequest,servletResponse);;
    }
}
