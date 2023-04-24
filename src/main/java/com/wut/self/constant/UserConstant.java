package com.wut.self.constant;

/**
 * 用户常量
 * @author zeng
 */
public interface UserConstant {

    /**
     * 用户登录状态
     */
    String USER_LOGIN_STATE = "userLoginState";

    /**
     * 用户默认权限
     */
    int DEFAULT_ROLE = 0;

    /**
     * 管理员权限
     */
    int ADMIN_ROLE = 1;

    /**
     * 用户默认头像
     */
    String USER_AVATAR_DEFAULT_URL = "https://typora-myself.oss-cn-hangzhou.aliyuncs.com/typora_img/Project--测试用头像.jpg";

    /**
     * redis 缓存key前缀
     */
    String REDIS_KEY_PREFIX = "link_partner:user:recommend:%s";
}