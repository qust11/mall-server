package com.ym.promotion.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ym.promotion.entity.Seckill;

import java.time.LocalDateTime;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author qushutao
 * @since 2026-07-03
 */
public interface SeckillMapper extends BaseMapper<Seckill> {

    int countBySkuAndTime(Long skuId, LocalDateTime startTime, LocalDateTime endTime);
}
