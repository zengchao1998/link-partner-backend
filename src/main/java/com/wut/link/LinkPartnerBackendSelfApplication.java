package com.wut.link;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.wut.self.mapper")
public class LinkPartnerBackendSelfApplication {

    public static void main(String[] args) {
        SpringApplication.run(LinkPartnerBackendSelfApplication.class, args);
    }

}
