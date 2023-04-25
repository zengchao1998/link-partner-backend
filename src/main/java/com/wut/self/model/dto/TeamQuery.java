package com.wut.self.model.dto;

import com.wut.self.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author zeng
 * 查询队伍信息的包装类(前端自主传值)
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TeamQuery extends PageRequest {

    private static final long serialVersionUID = 961309806720093153L;

    /**
     * id
     */
    private Long id;

    /**
     * 队伍名称
     */
    private String teamName;

    /**
     * 关键词(队伍名称 & 队伍名)
     */
    private String searchText;

    /**
     * 队伍描述
     */
    private String description;

    /**
     * 队伍最大人数
     */
    private Integer maxNum;

    /**
     * 创建用户id
     */
    private Long userId;

    /**
     * 0-公开状态, 1-私有状态, 2-加密状态
     */
    private Integer status;
}
