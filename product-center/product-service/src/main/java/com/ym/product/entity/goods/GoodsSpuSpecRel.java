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
 * 
 * </p>
 *
 * @author qushutao
 * @since 2026-06-18
 */
@TableName("goods_spu_spec_rel")
@ApiModel(value = "GoodsSpuSpecRel对象", description = "")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GoodsSpuSpecRel implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * spu表Id
     */
    @ApiModelProperty("spu表Id")
    private Long spuId;

    /**
     * spec表id
     */
    @ApiModelProperty("spec表id")
    private Long specId;
}
