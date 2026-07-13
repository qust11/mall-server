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
import java.time.LocalDateTime;

/**
 * <p>
 * 优惠券会员关联
 * </p>
 *
 * @author qushutao
 * @since 2026-07-03
 */
@TableName("coupon_member")
@ApiModel(value = "CouponMember对象", description = "")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponMember implements Serializable {

    @Serial
    private static final long serialVersionUID = -6406790215151709650L;

    private Long id;

    private Long couponId;

    @ApiModelProperty("用户d")
    private Long userId;

    @ApiModelProperty("领取时间")
    private LocalDateTime receivedTime;

    @ApiModelProperty("过期时间")
    private LocalDateTime expireTime;

    @ApiModelProperty("使用时间")
    private LocalDateTime useTime;

    @ApiModelProperty("订单id")
    private Long orderId;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("是否删除")
    @TableLogic(value = "0", delval = "1")
    private Integer isDeleted;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
