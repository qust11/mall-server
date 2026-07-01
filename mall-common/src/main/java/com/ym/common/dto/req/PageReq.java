package com.ym.common.dto.req;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qushutao
 * @since 2026-06-18 21:42
 **/
@AllArgsConstructor
@Data
@NoArgsConstructor
public class PageReq {

    private int current = 1;

    private int size = 8;
}
