package com.wut.self.common;

/**
 * @author zeng
 * 错误状态码
 */
public enum ErrorCode {

    SUCCESS(0, "ok", ""),
    PARAMS_ERROR(40000, "请求参数错误", ""),
    NULL_ERROR(40001, "请求数据为空", ""),
    EXECUTE_FAIL(40300, "请求执行失败", ""),
    NOT_LOGIN(40100, "未登录", ""),
    NO_AUTH(40101, "没有权限", ""),
    SYSTEM_ERROR(50000, "系统内部错误", "");

    /**
     * 状态码标识
     */
    private final Integer code;

    /**
     * 状态码信息
     */
    private final String message;

    /**
     * 状态码的详细描述
     */
    private final String description;

    ErrorCode(Integer code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
