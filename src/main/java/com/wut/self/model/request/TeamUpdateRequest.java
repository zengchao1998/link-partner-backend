package com.wut.self.model.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zeng
 */
@Data
public class TeamUpdateRequest implements Serializable {

    private static final long serialVersionUID = -7557717758601080662L;

    /**
     * 队伍id
     */
    private Long id;

    /**
     * 队伍名称
     */
    private String teamName;

    /**
     * 队伍描述
     */
    private String description;

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
