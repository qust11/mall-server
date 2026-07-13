package com.ym.common.exception;


import com.ym.common.constant.ResultCodeEnum;

/**
 * @author qushutao
 * @since 2026-07-13 13:02
 **/
public class BusinessException extends RuntimeException {
    private final ResultCodeEnum codeEnum;

    // 固定枚举提示
    public BusinessException(ResultCodeEnum codeEnum) {
        super(codeEnum.getMsg());
        this.codeEnum = codeEnum;
    }
    // 枚举 + 自定义错误文案
    public BusinessException(ResultCodeEnum codeEnum, String message) {
        super(message);
        this.codeEnum = codeEnum;
    }

    public ResultCodeEnum getCodeEnum() {
        return codeEnum;
    }
}
