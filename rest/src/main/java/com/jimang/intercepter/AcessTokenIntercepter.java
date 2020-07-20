package com.jimang.intercepter;


import com.google.gson.Gson;
import com.jimang.util.JsonMapper;
import com.jimang.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


/**
 * @Auther:wind
 * @Date:2020/7/19
 * @Version 1.0
 */
@Slf4j
public class AcessTokenIntercepter extends HandlerInterceptorAdapter {

    private RedisUtil redisUtil;


    public static final String REQ_ATTR_USER_ID = "user_id";

    private static final byte[] ACCESS_TOKEN_ERROR;

    static {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 3000);
        map.put("msg", "invalid access_token");
        ACCESS_TOKEN_ERROR = JsonMapper.writeValueAsBytes(map);
    }

    public AcessTokenIntercepter(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accessToken = request.getHeader("access_token");

        Long userId = (Long) redisUtil.get(accessToken);
        if (userId == null) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=utf-8");
            response.getOutputStream().write(ACCESS_TOKEN_ERROR);
            return false;
        } else {
            redisUtil.expire(accessToken, 7200);
            request.setAttribute(REQ_ATTR_USER_ID, userId);
        }
        return super.preHandle(request, response, handler);
    }
}
