package com.wut.self.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zeng
 * Redisson 配置类
 */
@Configuration
@ConfigurationProperties(prefix = "spring.redis")  // 从配置类中获取相关配置
@Data
public class RedissonConfig {

    private String host;

    private String port;

    @Bean
    public RedissonClient redissonClient() {
        // 1. 创建配置对象
        Config config = new Config();
        String redisAddress = String.format("redis://%s:%s", host, port);
        config.useSingleServer().setAddress(redisAddress).setDatabase(1);

        // 2. 创建一个redisson实例(Asyn, syn)
        return Redisson.create(config);
    }
}
