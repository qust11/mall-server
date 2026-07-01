package com.ym.product.dto.resp.goods.spu;


import com.ym.product.bo.goods.GoodsSpuCommonDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qushutao
 * @since 2026-06-18 12:07
 *   goods_name,
 *   spu_code,
 *   max_price,
 *   min_price,
 *   brand_name
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GoodsSpuResp extends GoodsSpuCommonDto {


    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * SPU编码
     */
    private String spuCode;


    private Long sales;

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
     * sku id
     */
    private Long skuId;
}
