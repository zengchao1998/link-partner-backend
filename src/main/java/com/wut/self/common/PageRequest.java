package com.wut.self.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zeng
 * 分页请求参数
 */
@Data
public class PageRequest implements Serializable {

    /**
     * 页面记录数目
     */
    protected long pageSize = 10;

    /**
     * 当前页码
     */
    protected long pageNum = 1;
}
