package com.jimang;

import com.github.fashionbrot.validated.config.annotation.EnableValidatedConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Auther:wind
 * @Date:2020/7/18
 * @Version 1.0
 */
@SpringBootApplication
@MapperScan("com.jimang.mapper")
@EnableValidatedConfig(fileName = "valid_zh_CN")
public class RestApplication {
    public static void main(String[] args) {
        SpringApplication.run(RestApplication.class, args);
    }
}
