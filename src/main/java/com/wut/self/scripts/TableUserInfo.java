package com.wut.self.scripts;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;


/**
 * @author zeng
 * Excel 表格用户数据的映射对象
 * 注解 @Excelproperty： 支持索引匹配 & 列名匹配
 */
@Data
public class TableUserInfo {

    /**
     * 用户校验码
     */
    @ExcelProperty("用户验证码")
    private String validateCode;

    /**
     * 用户名(用户昵称)
     */
    @ExcelProperty("用户昵称")
    private String username;
}