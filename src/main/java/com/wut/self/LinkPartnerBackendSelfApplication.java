package com.wut.self;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@MapperScan("com.wut.self.mapper")
@EnableScheduling  // 开启定时任务支持
public class LinkPartnerBackendSelfApplication {

    public static void main(String[] args) {
        SpringApplication.run(LinkPartnerBackendSelfApplication.class, args);
    }

}
