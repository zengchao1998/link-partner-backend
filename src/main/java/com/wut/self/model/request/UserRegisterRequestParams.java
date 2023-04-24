package com.wut.self.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册参数实体
 * @author zeng
 */
@Data
public class UserRegisterRequestParams implements Serializable {

    private static final long serialVersionUID = -2770235879832205022L;
    /**
     * 用户账号
     */
    private String userAccount;
    /**
     * 用户密码
     */
    private String userPassword;
    /**
     * 校验密码
     */
    private String checkPassword;
    /**
     * 用户验证码
     */
    private String validateCode;
}
