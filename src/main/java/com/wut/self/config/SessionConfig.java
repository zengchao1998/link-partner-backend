package com.wut.self.config;


import org.springframework.boot.autoconfigure.session.DefaultCookieSerializerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.session.MapSession;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.session.web.http.DefaultCookieSerializer;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 解决浏览器不支持跨域下的 cookie 操作
 */
@Configuration
@Profile({"dev", "test"})
public class SessionConfig {

    @Bean
    DefaultCookieSerializerCustomizer cookieSerializerCustomizer() {
        return new DefaultCookieSerializerCustomizer() {
            @Override
            public void customize(DefaultCookieSerializer cookieSerializer) {
                cookieSerializer.setSameSite("None");
                cookieSerializer.setUseSecureCookie(true); // 此项必须，否则set-cookie会被chrome浏览器阻拦
            }
        };
    }
}
