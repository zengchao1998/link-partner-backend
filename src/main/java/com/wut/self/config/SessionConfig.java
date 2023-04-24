package com.wut.self.config;


import org.springframework.boot.autoconfigure.session.DefaultCookieSerializerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.MapSession;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.session.web.http.DefaultCookieSerializer;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 解决浏览器不支持跨域下的 cookie 操作
 */
@Configuration
@EnableSpringHttpSession
public class SessionConfig {
    @Bean
    public SessionRepository<MapSession> sessionRepository() {
        return new MapSessionRepository(new ConcurrentHashMap<String, Session>());
    }

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
