package com.ym.product.dto.resp.goodspec;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qushutao
 * @since 2026-06-29 16:19
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GoodsSpecResp {


    private Long id;

    /**
     * 规格名称 如：颜色、尺码、内存
     */
    @ApiModelProperty("规格值 如：黑色、128、内存8G")
    private String specValue;

    private Integer sort;
}
