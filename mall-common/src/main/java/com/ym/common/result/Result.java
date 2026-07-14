package com.ym.common.result;


import com.ym.common.constant.ResultCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qushutao
 * @since 2026-06-16 20:54
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Result<T> {
    private Integer code;
    private String msg;
    private T data;

    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success", data);
    }

    public static <T> Result<T> success() {
        return new Result<>(200, "success", null);
    }

    public static <T> Result<T> fail(Integer code, String msg) {
        return new Result<>(code, msg, null);
    }

    public static <T> Result<T> fail(ResultCodeEnum resultCodeEnum) {
        return new Result<>(resultCodeEnum.getCode(), resultCodeEnum.getMsg(), null);
    }

    public static <T> Result<T> fail(ResultCodeEnum resultCodeEnum, String msg) {
        return new Result<>(resultCodeEnum.getCode(), msg, null);
    }

    public Boolean isSuccess() {
        return this.code == 200;
    }
}
