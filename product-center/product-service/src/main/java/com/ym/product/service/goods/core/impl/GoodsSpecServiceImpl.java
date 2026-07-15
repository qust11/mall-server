package com.ym.product.service.goods.core.impl;

import com.ym.product.dto.resp.goodspec.GoodsSpecResp;
import com.ym.product.entity.goods.GoodsSpec;
import com.ym.product.entity.goods.GoodsSpecValue;
import com.ym.product.mapper.goods.GoodsSpecMapper;
import com.ym.product.service.goods.core.IGoodsSpecService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ym.product.service.goods.core.IGoodsSpecValueService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 规格维度 服务实现类
 * </p>
 *
 * @author qushutao
 * @since 2026-06-18
 */
@Service
@AllArgsConstructor
public class GoodsSpecServiceImpl extends ServiceImpl<GoodsSpecMapper, GoodsSpec> implements IGoodsSpecService {

    private final IGoodsSpecValueService goodsSpecValueService;

    @Override
    public List<GoodsSpecResp> listGoodsSpecs() {
        List<GoodsSpecValue> list = goodsSpecValueService.list();
        return list.stream().map(c -> new GoodsSpecResp(c.getId(), c.getSpecValue(), c.getSort())).toList();
    }
}
