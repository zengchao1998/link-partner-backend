package com.wut.self.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author zeng
 * 队伍用户信息封装类(脱敏)
 */
@Data
public class TeamUserVo implements Serializable {

    private final long serialVersionUID = 7239594290049665530L;

    /**
     * id
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
     * 队伍最大人数
     */
    private Integer maxNum;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 创建用户id
     */
    private Long userId;

    /**
     * 0-公开状态, 1-私有状态, 2-加密状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 队伍用户信息列表
     */
    private List<UserVo> teamUsers;

    /**
     * 队伍创建人信息
     */
    private UserVo createUser;

    /**
     * 当前用户是否已加入队伍
     */
    private Boolean hasJoin;

    /**
     * 队伍人数信息
     */
    private Long teamNum;
}
