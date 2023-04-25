package com.wut.self.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zeng
 * 用户包装类(脱敏)
 */
@Data
public class UserVo implements Serializable {

    private final long serialVersionUID = 1834769981993127040L;

    /**
     * id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 登录账号
     */
    private String userAccount;

    /**
     * 头像
     */
    private String avatarUrl;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户状态
     */
    private Integer userStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 0-普通用户; 1-管理员; 2-vip用户
     */
    private Integer userRole;

    /**
     * 用户校验码
     */
    private String validateCode;

    /**
     * 用户标签
     */
    private String tags;

    /**
     * 用户个人简介
     */
    private String userProfile;
}
