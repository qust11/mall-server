package com.ym.promotion.converter;


import com.ym.promotion.dto.SeckillDto;
import com.ym.promotion.dto.req.SeckillReq;
import com.ym.promotion.dto.resp.SeckillResp;
import com.ym.promotion.entity.Promotion;
import com.ym.promotion.entity.Seckill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author qushutao
 * @since 2026-06-25 22:50
 **/
@Mapper
public interface SeckillConverter {

    SeckillConverter INSTANCE = Mappers.getMapper(SeckillConverter.class);


    Seckill toSeckill(SeckillReq seckillReq);

    @Mappings({
            @Mapping(source = "seckill.id", target = "id"),
    })
    SeckillResp toSeckillResp(Promotion promotion, Seckill seckill);

    SeckillDto toSeckillDto(Seckill seckill);

    List<SeckillDto> batchToSeckillDto(List<Seckill> seckills);
}
