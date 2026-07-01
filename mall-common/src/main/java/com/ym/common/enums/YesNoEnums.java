package com.ym.common.enums;


import lombok.Getter;

/**
 * @author qushutao
 * @since 2026-07-01 12:19
 **/
@Getter
public enum YesNoEnums {

    YES(1, "是"),
    NO(0, "否");

    private final Integer code;

    private final String desc;

    YesNoEnums(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
