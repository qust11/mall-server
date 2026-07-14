package com.ym.promotion.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 秒杀商品
 * </p>
 *
 * @author qushutao
 * @since 2026-07-03
 */
@ApiModel(value = "Seckill对象", description = "")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Seckill implements Serializable {

    @Serial
    private static final long serialVersionUID = -8954331162981651501L;

    private Long id;

    /**
     * 活动id
     */
    @ApiModelProperty("活动id")
    private Long promotionId;

    /**
     * 秒杀价格
     */
    @ApiModelProperty("秒杀价格")
    private Long seckillPrice;

    /**
     * 秒杀库存
     */
    @ApiModelProperty("秒杀库存")
    private Integer seckillStock;

    /**
     * 剩余库存
     */
    @ApiModelProperty("剩余库存")
    private Integer remainStock;

    /**
     * 秒杀商品
     */
    @ApiModelProperty("秒杀商品")
    private Long skuId;

    /**
     * 每人限购
     */
    @ApiModelProperty("每人限购")
    private Integer perLimit;

    @TableLogic(value = "0", delval = "1")
    @ApiModelProperty("逻辑删除")
    private Integer isDeleted;
}
