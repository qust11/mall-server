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
 * @since 2026-06-18
 */
@TableName("goods_spec")
@ApiModel(value = "GoodsSpec对象", description = "规格维度")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GoodsSpec implements Serializable {

    @Serial
    private static final long serialVersionUID = 5961523193358519976L;

    private Long id;

    /**
     * 规格名称 如：颜色、尺码、内存
     */
    @ApiModelProperty("规格名称 如：颜色、尺码、内存")
    private String specName;

    private Integer sort;

    private LocalDateTime createTime;

}
