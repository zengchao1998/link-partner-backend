package com.wut.self.exception;

import com.wut.self.common.ErrorCode;

/**
 * @author zeng
 * 自定义业务异常类
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = -2643305081572497304L;

    /**
     * 异常状态码
     */
    private final int code;

    /**
     * 异常详细信息
     */
    private final String description;

    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    /**
     * 自定义描述信息
     * @param errorCode 错误枚举类
     * @param description 自定义描述
     */
    public BusinessException(ErrorCode errorCode, String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
