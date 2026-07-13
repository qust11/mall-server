package com.ym.promotion.converter;


import com.ym.promotion.dto.req.CouponReq;
import com.ym.promotion.dto.resp.CouponResp;
import com.ym.promotion.entity.Coupon;
import com.ym.promotion.entity.Promotion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author qushutao
 * @since 2026-06-25 22:50
 **/
@Mapper
public interface CouponConverter {

    CouponConverter INSTANCE = Mappers.getMapper(CouponConverter.class);

    @Mappings({
            @Mapping(target = "spuIds", ignore = true),
            @Mapping(target = "categoryIds", ignore = true),
            @Mapping(target = "remainStock", source = "stock")
    })
    Coupon toCoupon(CouponReq couponReq);


    @Mappings({
            @Mapping(target = "id", source = "coupon.id"),
            @Mapping(target = "spuIds", ignore = true),
            @Mapping(target = "categoryIds", ignore = true),
    })
    CouponResp toCouponResp(Promotion promotion, Coupon coupon);
}
