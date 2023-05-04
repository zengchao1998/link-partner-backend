package com.wut.self.constant;

/**
 * @author zeng
 */
public interface RedisConstant {

    /**
     * redis 缓存key前缀
     */
    String REDIS_KEY_USER_PREFIX = "link_partner:user:%s";

    /**
     * redis 队伍key前缀
     */
    String REDIS_KEY_TEAM_PREFIX = "link_partner:team:%s";
}
