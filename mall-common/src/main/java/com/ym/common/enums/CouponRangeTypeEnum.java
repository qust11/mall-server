package com.ym.common.enums;


import lombok.Getter;

/**
 * @author qushutao
 * @since 2026-07-03 16:29
1：全平台 2: 全店 3: 指定类目 4: 指定商品
 **/
@Getter
public enum CouponRangeTypeEnum {

    ALL_PLANTFORM(1, "全平台"),
    ALL_STORE(2, "全店"),
    SPECIFIC_CATEGORY(3, "指定类目"),
    SPECIFIC_PRODUCT(4, "指定商品"),
    ;
    private final Integer code;
    private final String desc;

    CouponRangeTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static CouponRangeTypeEnum getByCode(Integer code) {
        for (CouponRangeTypeEnum value : CouponRangeTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
