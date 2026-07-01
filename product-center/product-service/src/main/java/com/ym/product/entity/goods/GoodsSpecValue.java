package com.ym.product.entity.goods;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 规格可选值
 * </p>
 *
 * @author qushutao
 * @since 2026-06-18
 */
@TableName("goods_spec_value")
@ApiModel(value = "GoodsSpecValue对象", description = "规格可选值")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GoodsSpecValue implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 关联规格项ID
     */
    @ApiModelProperty("关联规格项ID")
    private Integer specId;

    /**
     * 规格值
     */
    @ApiModelProperty("规格值")
    private String specValue;

    /**
     * 规格图片（颜色图）
     */
    @ApiModelProperty("规格图片（颜色图）")
    private String specImage;

    private Integer sort;
}
