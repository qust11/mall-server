package com.ym.product.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author qushutao
 * @since 2026-06-18 21:43
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GoodsUpdateReq {

    /**
     * 所属分类
     */
    @ApiModelProperty("所属分类")
    private Long categoryId;

    /**
     * 商品标题/名称
     */
    @ApiModelProperty("商品标题/名称")
    private String goodsName;

    /**
     * 副标题卖点
     */
    @ApiModelProperty("副标题卖点")
    private String subTitle;

    /**
     * 品牌ID（可单独建brand表）
     */
    @ApiModelProperty("品牌ID（可单独建brand表）")
    private Long brandId;

    /**
     * 商品主图
     */
    @ApiModelProperty("商品主图")
    private String mainImg;

    /**
     * 商品详情富文本
     */
    @ApiModelProperty("商品详情富文本")
    private String goodsDesc;

    /**
     * 市场价划线价
     */
    @ApiModelProperty("市场价划线价")
    private BigDecimal marketPrice;

    /**
     * 商品重量g
     */
    @ApiModelProperty("商品重量g")
    private Double weight;
//
//    /**
//     * 0草稿 1上架 2下架
//     */
//    @ApiModelProperty("0草稿 1上架 2下架")
//    private Integer status;

    /**
     * 是否热销
     */
    @ApiModelProperty("是否热销")
    private Integer isHot;

    /**
     * 是否新品
     */
    @ApiModelProperty("是否新品")
    private Integer isNew;

    /**
     * 标签
     */
    @ApiModelProperty("标签")
    private String tagText;
}
