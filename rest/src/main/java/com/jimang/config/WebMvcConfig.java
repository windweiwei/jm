package com.jimang.config;

import com.jimang.intercepter.AcessTokenIntercepter;
import com.jimang.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.spring.web.SpringfoxWebMvcConfiguration;

/**
 * @Auther:wind
 * @Date:2020/7/19
 * @Version 1.0
 */
@SpringBootApplication
@ConditionalOnClass(SpringfoxWebMvcConfiguration.class)
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CorsConfig());
        registry.addInterceptor(new AcessTokenIntercepter(redisUtil))
                .addPathPatterns("/api/v1/**")
                .excludePathPatterns("/api/v1/users/auth_code",
                        "/api/v1/users/forgot_password/auth_code",
                        "/api/v1/users/forgot_password/update",
                        "/api/v1/users/save",
                        "/api/v1/users/login");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
