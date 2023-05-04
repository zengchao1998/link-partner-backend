package com.wut.self.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zeng
 * 删除的请求参数封装类
 */
@Data
public class DeleteRequest implements Serializable {

    private static final long serialVersionUID = -5893806182377990131L;

    /**
     * 队伍id
     */
    private Long teamId;
}
