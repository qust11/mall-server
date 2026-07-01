package com.ym.product.dto.resp.goods.spu;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author qushutao
 * @since 2026-06-18 12:07
 *  详情专用
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GoodsSpuDetailResp {


    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * SPU编码
     */
    private String spuCode;

    /**
     * 品牌名称
     */
    private Long brandId;

    /**
     * 库存总数量
     */
    private Integer stockCount;

    /**
     * spu id
     */
    private Long id;

    /**
     * 副标题卖点
     */
    private String subTitle;

    /**
     * 标签
     */
    private String tagText;

    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 主图
     */
    private String mainImg;

    /**
     * 0草稿 1上架 2下架
     */
    @ApiModelProperty("0草稿 1上架 2下架")
    private Integer status;

    @ApiModelProperty("市场价划线价")
    private BigDecimal marketPrice;

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

}
