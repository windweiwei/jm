package com.jimang.config;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther:wind
 * @Date:2020/7/19
 * @Version 1.0
 */
public class CorsConfig extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type,content-type,"
                + "app_key,app_secret,access_token");
        response.setHeader("Custom-Header", "Own-Data");
        response.setHeader("Access-Control-Expose-Headers",
                "Content-Type, X-Requested-With,Accept,content-type,app_key,app_secret");
        return super.preHandle(request, response, handler);
    }
}
