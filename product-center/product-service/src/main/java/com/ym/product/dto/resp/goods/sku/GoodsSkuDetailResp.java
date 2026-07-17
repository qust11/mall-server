package com.ym.product.dto.resp.goods.sku;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qushutao
 * @since 2026-06-29 14:10
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsSkuDetailResp {

    private Long id;

    /**
     * 所属SPU
     */
    @ApiModelProperty("所属SPU")
    private Long spuId;

    /**
     * SKU编码，唯一
     */
    @ApiModelProperty("SKU编码，唯一")
    private String skuCode;

    /**
     * sku名称
     */
    @ApiModelProperty("sku名称")
    private String skuName;

    /**
     * 商品条码
     */
    @ApiModelProperty("商品条码")
    private String barCode;

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
    @ApiModelProperty("售价 单位:分")
    private Long price;

    /**
     * 成本价格
     */
    @ApiModelProperty("优惠价")
    private Long discountPrice;

    /**
     * 总库存
     */
    @ApiModelProperty("总库存")
    private Integer totalStock;

    /**
     * sku销售量
     */
    @ApiModelProperty("sku销售量")
    private Integer sales;
}
