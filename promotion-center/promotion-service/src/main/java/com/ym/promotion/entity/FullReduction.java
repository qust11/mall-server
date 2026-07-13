package com.ym.promotion.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 满减满折
 * </p>
 *
 * @author qushutao
 * @since 2026-07-03
 */
@TableName("full_reduction")
@ApiModel(value = "FullDiscount对象", description = "")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullReduction implements Serializable {

    @Serial
    private static final long serialVersionUID = 4869802567700172629L;

    private Long id;

    /**
     * 活动预算
     */
    @ApiModelProperty("活动预算")
    private Long promotionId;

    /**
     * 折扣类型 1:满减 2:满折
     */
    @ApiModelProperty("折扣类型 1:满减 2:满折")
    private Integer discountType;

    /**
     * 满足金额
     */
    @ApiModelProperty("满足金额")
    private Long useThreshold;

    /**
     * 减免金额
     */
    @ApiModelProperty("减免金额")
    private Long reduceAmount;

    /**
     *折扣
     */
    @ApiModelProperty("折扣值")
    private Long discountRate;

    @TableLogic(value = "0", delval = "1")
    @ApiModelProperty("逻辑删除")
    private Integer isDeleted;
}

