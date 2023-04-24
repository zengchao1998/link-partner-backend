package com.wut.self.exception;


import com.wut.self.common.BaseResponse;
import com.wut.self.common.ErrorCode;
import com.wut.self.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author zeng
 * 全局异常处理组件
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 统一处理业务异常，过滤返回给前端的异常信息 (通用返回类型)
     */
    @ExceptionHandler(BusinessException.class)
    public BaseResponse<Object> handleBusinessException(BusinessException e) {
        log.error("businessException: " + e.getMessage(), e);
        return ResultUtils.error(e.getCode(), e.getMessage(), e.getDescription());
    }

    /**
     * 统一处理系统内部运行时异常 (通用返回类型)
     */
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<Object> handleRuntimeException(RuntimeException e) {
        log.info("runtimeException: ", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR);
    }
}
