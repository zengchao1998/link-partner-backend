package com.wut.self.utils;

import com.wut.self.common.BaseResponse;
import com.wut.self.common.ErrorCode;

/**
 * @author zeng
 * 返回值工具类
 */
public class ResultUtils {

    /**
     * 执行成功
     * @param data 执行结果
     * @return 通用返回类型
     * @param <T> 执行结果数据类型
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "ok");
    }

    /**
     * 执行失败
     * @param errorCode 执行错误状态码
     * @param description 自定义描述信息
     * @return 通用返回类型
     */
    public static BaseResponse<Object> error(ErrorCode errorCode, String description) {
        return new BaseResponse<>(errorCode, description);
    }

    /**
     * 执行失败
     * @param errorCode 执行错误状态码
     * @return 通用返回类型
     */
    public static BaseResponse<Object> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    /**
     * 执行失败
     * @param code 错误状态码
     * @param message 错误信息
     * @return 通用返回类型
     */
    public static BaseResponse<Object> error(int code, String message, String description) {
        return new BaseResponse<>(code, message, description);
    }
}
