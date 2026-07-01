package com.ym.product.service.goods.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ym.product.dto.resp.category.PrimaryCategoryResp;
import com.ym.product.entity.goods.GoodsCategory;
import com.ym.product.mapper.goods.GoodsCategoryMapper;
import com.ym.product.service.goods.IGoodsCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品分类 服务实现类
 * </p>
 *
 * @author qushutao
 * @since 2026-06-18
 */
@Service
public class GoodsCategoryServiceImpl extends ServiceImpl<GoodsCategoryMapper, GoodsCategory> implements IGoodsCategoryService {

    @Override
    public List<PrimaryCategoryResp> listPrimaryCategory() {
        List<GoodsCategory> list = list(new LambdaQueryWrapper<GoodsCategory>()
                .eq(GoodsCategory::getParentId, 0));
        return list.stream()
                .map(item -> new PrimaryCategoryResp(item.getId(), item.getCategoryName()))
                .collect(Collectors.toList());
    }
}
