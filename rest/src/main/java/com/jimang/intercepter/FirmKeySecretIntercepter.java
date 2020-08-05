package com.jimang.intercepter;

import com.jimang.cache.FirmCache;
import com.jimang.model.Firms;
import com.jimang.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther:wind
 * @Date:2020/7/25
 * @Version 1.0
 */
@Slf4j
public class FirmKeySecretIntercepter extends HandlerInterceptorAdapter {

    private final FirmCache firmCache;

    public static final String REQ_ATTR_FIRM_ID = "firm_id";

    private static final byte[] VALID_ERROR;

    static {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 3001);
        map.put("msg", "invalid app_key or app_secret");
        VALID_ERROR = JsonMapper.writeValueAsBytes(map);
    }

    public FirmKeySecretIntercepter(FirmCache firmCache) {
        this.firmCache = firmCache;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String appKey = request.getHeader("app_key");
        Firms firm = null;
        if (appKey != null) {
            firm = firmCache.getDomain(request.getHeader("app_key"));
            log.debug("app_key is null");
        }
        if (null == firm || !firm.getAppSecret().equals(request.getHeader("app_secret"))) {
            response.setContentType("application/json;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            response.getOutputStream().write(VALID_ERROR);
            return false;
        } else {
            request.setAttribute(REQ_ATTR_FIRM_ID, firm.getId());
        }

        return super.preHandle(request, response, handler);
    }
}
