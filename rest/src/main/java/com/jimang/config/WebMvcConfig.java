package com.jimang.config;

import com.jimang.cache.FirmCache;
import com.jimang.intercepter.AcessTokenIntercepter;
import com.jimang.intercepter.FirmKeySecretIntercepter;
import com.jimang.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.spring.web.SpringfoxWebMvcConfiguration;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

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
    @Autowired
    private FirmCache firmCache;

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
        registry.addInterceptor(new FirmKeySecretIntercepter(firmCache))
                .addPathPatterns("/api/v1/**")
                .excludePathPatterns("/api/v1/users/head_portrait/download/**", "/api/v1/devices/cover/download/**");
        registry.addInterceptor(new AcessTokenIntercepter(redisUtil))
                .addPathPatterns("/api/v1/**")
                .excludePathPatterns("/api/v1/users/auth_code",
                        "/api/v1/users/forgot_password/auth_code",
                        "/api/v1/users/forgot_password/update",
                        "/api/v1/users/save",
                        "/api/v1/users/head_portrait/download/**",
                        "/api/v1/devices/cover/download/**",
                        "/api/v1/users/login");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(3000);
        requestFactory.setReadTimeout(2000);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return restTemplate;
    }

    @Bean
    public HttpMessageConverter<String> responseBodyStringConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        return converter;
    }

    /**
     * 修改StringHttpMessageConverter默认配置
     *
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(responseBodyStringConverter());
    }


}
