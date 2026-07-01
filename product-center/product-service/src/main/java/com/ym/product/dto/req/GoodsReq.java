package com.ym.product.dto.req;


import com.ym.common.dto.req.PageReq;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qushutao
 * @since 2026-06-18 21:43
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GoodsReq extends PageReq {

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 状态
     */
    private String saleStatus;
}
