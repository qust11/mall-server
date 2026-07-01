package com.ym.product.service.goods.impl;

import com.ym.product.entity.goods.GoodsSpu;
import com.ym.product.mapper.goods.GoodsSpuMapper;
import com.ym.product.service.goods.IGoodsSpuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * SPU绑定规格 服务实现类
 * </p>
 *
 * @author qushutao
 * @since 2026-06-18
 */
@Service
public class GoodsSpuServiceImpl extends ServiceImpl<GoodsSpuMapper, GoodsSpu> implements IGoodsSpuService {

    @Override
    public String getMaxSpuCode() {
        String maxCode = baseMapper.getMaxSpuCode();
        if (StringUtils.isBlank(maxCode)){
            return "000001";
        }
        return maxCode;
    }
}
