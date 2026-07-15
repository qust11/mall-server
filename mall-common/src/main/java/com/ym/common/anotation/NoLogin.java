package com.ym.common.anotation;


/**
 *
 * @author qushutao
 * @since 2026-07-14 16:07
 **/

import java.lang.annotation.*;

/**
 * 标记无需登录即可访问的接口
 * 可加在 Controller 类 / Controller 方法上
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoLogin {
}