package com.ym.product.entity.elastic;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 *
 * @author qushutao
 * @since 2026-07-15 14:55
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "goods_spu")
public class GoodsSpuDoc {

    @Id
    private Long id;

    @Field(type = FieldType.Text, analyzer = "ik_smart",copyTo = "all")
    private String goodsName;
    /**
     * 品牌名称
     */

    @Field(type = FieldType.Keyword,copyTo = "all")
    private String brandName;

    /**
     * spu名称
     */
    @Field(type = FieldType.Long)
    private Long brandId;

    /**
     * sku数量
     */
    @Field(type = FieldType.Long)
    private Integer skuCount;

    /**
     * 库存总数量
     */
    @Field(type = FieldType.Long)
    private Integer stockCount;
    /**
     * 最高价
     */
    @Field(type = FieldType.Long)
    private Long maxPrice;

    /**
     * 最低价
     */
    @Field(type = FieldType.Long)
    private Long minPrice;


    @Field(type = FieldType.Long)
    private Long skuId;

    /**
     * 副标题卖点
     */
    @Field(type = FieldType.Text, analyzer = "ik_smart",copyTo = "all")
    private String subTitle;

    /**
     * 标签
     */
    private String tagText;

    /**
     * 分类id
     */
    @Field(type = FieldType.Long)
    private Long categoryId;

    @Field(type = FieldType.Keyword)
    private String categoryName;

    @Field(type = FieldType.Keyword, index = false)
    private String mainImg;

    @Field(type = FieldType.Integer)
    private Integer isHot;

    @Field(type = FieldType.Integer)
    private Integer isNew;

    @Field(type = FieldType.Long)
    private Long sales;

    @Field(type = FieldType.Text,analyzer = "ik_smart")
    private String all;

    @Field(type = FieldType.Keyword)
    private String spuCode;
}
