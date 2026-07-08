package com.ym.promotion.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

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

    private Long userId;
}
