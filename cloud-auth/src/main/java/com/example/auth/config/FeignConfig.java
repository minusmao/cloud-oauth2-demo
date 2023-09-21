package com.example.auth.config;

import feign.Logger;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenFeign 配置
 *
 * @author minus
 * @since 2023/9/12 16:07
 */
@Configuration
@EnableFeignClients(basePackages = "com.example.auth.client")
public class FeignConfig {

    /**
     * 配置 OpenFeign 的日志级别<br>
     * 参考：<a href="https://blog.csdn.net/wqc19920906/article/details/123837358">OpenFeign 配置日志输出</a>
     *
     * @return Level
     */
    @Bean
    Logger.Level feignLevel() {
        return Logger.Level.FULL;
    }

}
