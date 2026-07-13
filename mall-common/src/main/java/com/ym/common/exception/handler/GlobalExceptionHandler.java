package com.ym.common.exception.handler;


import com.ym.common.constant.ResultCodeEnum;
import com.ym.common.exception.BusinessException;
import com.ym.common.result.Result;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author qushutao
 * @since 2026-07-13 13:07
 **/
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    // 1. 业务自定义异常（主动throw的）
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e) {
        log.warn("业务异常：code={}, msg={}", e.getCodeEnum().getCode(), e.getMessage());
        return Result.fail(e.getCodeEnum(), e.getMessage());
    }

    // 2. 参数校验异常 @Valid / @NotBlank 等
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleValidException(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldError().getDefaultMessage();
        log.warn("参数校验异常：{}", msg);
        return Result.fail(ResultCodeEnum.PARAM_ERROR, msg);
    }

    // 3. 远程调用异常（OpenFeign）
    @ExceptionHandler(FeignException.class)
    public Result<?> handleFeignException(FeignException e) {
        log.error("远程调用服务异常", e);
        return Result.fail(ResultCodeEnum.REMOTE_CALL_ERROR);
    }

    // 4. 兜底：所有未知系统异常
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        log.error("系统未知异常", e);
        return Result.fail(ResultCodeEnum.SYS_ERROR);
    }
}
