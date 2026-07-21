package com.ym.promotion.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author qushutao
 * @since 2026-07-20 18:56
 **/
@TableName("coupon_category")
@ApiModel(value = "CouponCategory对象", description = "")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponCategory {

    private Long id;

    private Long couponId;

    private Long categoryId;
}
