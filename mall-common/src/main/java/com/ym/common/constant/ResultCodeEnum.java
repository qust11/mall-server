package com.ym.common.constant;


import lombok.Getter;

/**
 * @author qushutao
 * @since 2026-07-13 13:05
 **/
@Getter
public enum ResultCodeEnum {
    // 通用 1xxx
    SUCCESS(200, "操作成功"),
    PARAM_ERROR(400, "请求参数非法"),
    NOT_LOGIN(401, "用户未登录"),
    NO_PERMISSION(403, "权限不足"),
    SYS_ERROR(500, "服务器内部异常"),
    REMOTE_CALL_ERROR(506, "远程服务调用失败"),

    // 用户 2xxx
    USER_NOT_EXIST(20001, "用户不存在"),
    PHONE_EXIST(20002, "手机号已注册"),

    // 商品 3xxx
    GOODS_NOT_EXIST(30001, "商品不存在"),
    STOCK_SHORT(30002, "库存不足"),
    GOODS_SKU_CHANGE_FAILED(31002, "商品SKU规格库存"),

    // 订单 4xxx
    ORDER_NOT_EXIST(40001, "订单不存在"),
    ORDER_CANCELLED(40002, "订单已取消"),

    // 促销 5xxx
    ACTIVITY_NOT_EXIST(50001, "活动不存在"),
    ACTIVITY_NOT_START(50002, "活动未开始"),
    ACTIVITY_START_TIME_ERR(50004, "活动开始时间不能晚于结束时间"),
    ACTIVITY_START_TIME_EARLY(50005, "活动开始时间应晚于当前时间"),
    ACTIVITY_CREATE_FAILED(50006, "活动创建失败"),
    COUPON_RECEIVE_HAS_ERROR(51000, "优惠券领取失败"),
    COUPON_EXPIRE(51003, "优惠券已过期"),

    // 购物车 6xxx
    CART_NOT_EXIST(60001, "购物车不存在"),
    CART_SKU_MAX_NUM(60002, "购物车商品数量超过最大数量"),
    ;

    private final Integer code;
    private final String msg;

    ResultCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
