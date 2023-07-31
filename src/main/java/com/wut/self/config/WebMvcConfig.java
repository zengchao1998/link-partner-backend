package com.wut.self.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static com.wut.self.constant.DevConstant.*;

/**
 * @author zeng
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 跨域配置
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 设置允许跨域的路径
        registry.addMapping("/**")
                // 设置允许跨域请求的域名
                // 当 Credentials 为true 时，Origin 不能为星号，需为具体的ip地址 [如果接口不带 cookie,ip 无需设成具体ip]
                .allowedOrigins(DEV_URL, DEV_URL_SUP, PROD_URL)
                // 设置允许的请求方法
                .allowedMethods("*")
                // 是否允许证书(携带cookie信息)
                .allowCredentials(true)
                // 设置允许的方法
                // 跨域允许时间
                .maxAge(3600);
    }
}