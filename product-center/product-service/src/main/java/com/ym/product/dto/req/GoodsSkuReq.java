package com.ym.product.dto.req;


import com.ym.common.dto.req.PageReq;
import io.swagger.annotations.ApiModelProperty;
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
public class GoodsSkuReq  {


    @ApiModelProperty("sku名称")
    private String skuName;

    /**
     * SKU编码，唯一
     */
    @ApiModelProperty("SKU编码，唯一")
    private String skuCode;

    /**
     * 规格值ID组合，逗号 例：1,5
     */
    @ApiModelProperty("规格值ID组合，逗号 例：1,5")
    private String specIds;


    /**
     * 当前SKU规格图
     */
    @ApiModelProperty("当前SKU规格图")
    private String skuImg;

    /**
     * 价格 单位:分
     */
    @ApiModelProperty("价格 单位:分")
    private Long price;

    /**
     * 现有库存
     */
    @ApiModelProperty("现有库存")
    private Integer stock;

    /**
     * 成本价格
     */
    @ApiModelProperty("成本价格 单位：分")
    private Long costPrice;

}
