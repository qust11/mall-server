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
 * 规格维度
 * </p>
 *
 * @author qushutao
 * @since 2026-06-26
 */
@TableName("goods_brand")
@ApiModel(value = "GoodsBrand对象", description = "规格维度")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GoodsBrand implements Serializable {

    @Serial
    private static final long serialVersionUID = 1191399728388752563L;

    private Long id;

    /**
     * 品牌名称
     */
    @ApiModelProperty("品牌名称")
    private String brandName;

    private Integer sort;

    private LocalDateTime createTime;

}
