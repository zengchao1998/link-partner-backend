package com.wut.self.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录参数实体
 * @author zeng
 */
@Data
public class UserLoginRequestParams implements Serializable {
    private static final long serialVersionUID = -903344248002350981L;
    /**
     * 用户账户
     */
    private String userAccount;
    /**
     * 用户密码
     */
    private String userPassword;
}
