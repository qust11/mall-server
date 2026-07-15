package com.ym.product.entity.goods;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * SPU绑定规格
 * </p>
 *
 * @author qushutao
 * @since 2026-06-18
 */
@TableName("goods_spu")
@ApiModel(value = "GoodsSpu对象", description = "SPU绑定规格")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GoodsSpu implements Serializable {

    @Serial
    private static final long serialVersionUID = -3144460116148059993L;

    private Long id;

    /**
     * SPU编码，唯一
     */
    @ApiModelProperty("SPU编码，唯一")
    private String spuCode;

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
    @ApiModelProperty("品牌ID")
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
     * 总销量
     */
    @ApiModelProperty("总销量")
    private Long sales;


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
