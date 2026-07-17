package com.ym.product.entity.goods;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 商品SKU规格库存表
 * </p>
 *
 * @author qushutao
 * @since 2026-06-18
 */
@TableName("goods_sku")
@ApiModel(value = "GoodsSku对象", description = "商品SKU规格库存表")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GoodsSku implements Serializable {

    @Serial
    private static final long serialVersionUID = 7642626772995609936L;

    private Long id;

    /**
     * 所属SPU
     */
    @ApiModelProperty("所属SPU")
    private Long spuId;

    @ApiModelProperty("sku名称")
    private String skuName;

    /**
     * SKU编码，唯一
     */
    @ApiModelProperty("SKU编码，唯一")
    private String skuCode;

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
    @ApiModelProperty("价格 单位:分")
    private Long price;

    /**
     * 总库存
     */
    @ApiModelProperty("总库存")
    private Integer totalStock;

    /**
     * 剩余/可用 库存
     */
    @ApiModelProperty("剩余/可用 库存")
    private Integer remainStock;

    /**
     * 锁单库存
     */
    @ApiModelProperty("锁单库存")
    private Integer lockStock;

    /**
     * 成本价格
     */
    @ApiModelProperty("成本价格")
    private Long costPrice;

    /**
     * sku销售量
     */
    @ApiModelProperty("sku销售量")
    private Integer sales;

    /**
     * 0不可售 1正常
     */
    @ApiModelProperty("0不可售 1正常")
    private Integer status;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;
}
