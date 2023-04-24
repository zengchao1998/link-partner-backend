package com.wut.self.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zeng
 * 通用返回对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = 8193062970791920832L;

    private int code;

    private T data;

    private String message;

    private String description;

    public BaseResponse(int code, T data, String message) {
        this(code, data, message, "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage(), errorCode.getDescription());
    }

    public BaseResponse(ErrorCode errorCode, String description) {
        this(errorCode.getCode(), null, errorCode.getMessage(), description);
    }

    public BaseResponse(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }
}
