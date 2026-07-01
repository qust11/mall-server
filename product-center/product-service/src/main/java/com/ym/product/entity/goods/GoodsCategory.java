package com.ym.product.entity.goods;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 商品分类
 * </p>
 *
 * @author qushutao
 * @since 2026-06-18
 */
@TableName("goods_category")
@ApiModel(value = "GoodsCategory对象", description = "商品分类")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GoodsCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 父分类ID，0=一级分类
     */
    @ApiModelProperty("父分类ID，0=一级分类")
    private Long parentId;

    /**
     * 分类名称
     */
    @ApiModelProperty("分类名称")
    private String categoryName;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

    /**
     * 0禁用 1启用
     */
    @ApiModelProperty("0禁用 1启用")
    private Byte status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
