package com.ym.product.dto.resp.goods.spu;


import com.ym.product.dto.resp.goods.sku.GoodsSkuDetailResp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
public class GoodsSpuUserDetailResp{

    private Long id;
    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * spu名称
     */
    private Long brandId;

    /**
     * sku数量
     */
    private Integer skuCount;

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


    private List<GoodsSkuDetailResp> skuList;

}
