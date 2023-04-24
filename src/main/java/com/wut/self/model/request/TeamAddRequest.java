package com.wut.self.model.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zeng
 * 创建队伍的请求参数
 */
@Data
public class TeamAddRequest implements Serializable {

    private static final long serialVersionUID = -3556445236188302763L;

    /**
     * 队伍名称
     */
    private String teamName;

    /**
     * 队伍描述
     */
    private String description;

    /**
     * 队伍最大人数
     */
    private Integer maxNum;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 0-公开状态, 1-私有状态, 2-加密状态
     */
    private Integer status;

    /**
     * 队伍申请密码
     */
    private String teamPassword;
}
