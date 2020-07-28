package com.jimang.cache;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jimang.mapper.FirmsMapper;
import com.jimang.model.Firms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther:wind
 * @Date:2020/7/25
 * @Version 1.0
 */
@Component
public class FirmCache {
    private Map<String, Firms> cacheItems;

    @Autowired
    private FirmsMapper firmsMapper;


    public FirmCache() {
        cacheItems = new ConcurrentHashMap<>();
    }

    /**
     * 判断域名
     */
    public Firms getDomain(String appKey) {
        Firms domain = cacheItems.get(appKey);
        if (domain == null) {
            QueryWrapper<Firms> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("app_key", appKey);
            domain = firmsMapper.selectOne(queryWrapper);
            if (domain == null) {
                return null;
            }
            domain.setAppKey(appKey);
            cacheItems.put(appKey, domain);
        }
        return domain;
    }
}
