package com.ym.product;

import com.ym.common.dto.req.PageReq;
import com.ym.product.bo.goods.GoodsNumBO;
import com.ym.product.entity.elastic.GoodsSpuDoc;
import com.ym.product.entity.goods.GoodsSpu;
import com.ym.product.repository.GoodsSpuRepository;
import com.ym.product.service.elastic.GoodsSpuEsService;
import com.ym.product.service.goods.core.IGoodsBrandService;
import com.ym.product.service.goods.core.IGoodsCategoryService;
import com.ym.product.service.goods.core.IGoodsSkuService;
import com.ym.product.service.goods.core.IGoodsSpuService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @author qushutao
 * @since 2026-07-15 11:44
 **/
@SpringBootTest
public class EsTest {
    @Autowired
    private GoodsSpuEsService goodsSpuEsService;
    @Autowired
    private IGoodsSpuService goodsSpuService;
    @Autowired
    private IGoodsCategoryService goodsCategoryService;
    @Autowired
    private IGoodsBrandService goodsBrandService;
    @Autowired
    private IGoodsSkuService goodsSkuService;
    @Test
    public void getHeima() throws IOException {
        List<GoodsSpu> list = goodsSpuService.list();
        List<Long> spuIds = list.stream().map(GoodsSpu::getId).collect(Collectors.toList());
        List<GoodsNumBO> goodsNumBOList = goodsSkuService.getGoodsNumBOListBySpuIds(spuIds);
        Map<Long, GoodsNumBO> skuIdToNumMap = goodsNumBOList.stream().collect(Collectors.toMap(GoodsNumBO::getSpuId, Function.identity(), (v1, v2) -> v1));
        for (GoodsSpu goodsSpu : list) {

            GoodsSpuDoc entity = new GoodsSpuDoc();
            GoodsNumBO goodsNumBO = skuIdToNumMap.get(goodsSpu.getId());
            entity.setId(goodsSpu.getId());
            entity.setGoodsName(goodsSpu.getGoodsName());
            entity.setBrandName(goodsBrandService.getById(goodsSpu.getBrandId()).getBrandName());
            entity.setBrandId(goodsSpu.getBrandId());
            entity.setSubTitle(goodsSpu.getSubTitle());
            entity.setMainImg(goodsSpu.getMainImg());
            entity.setCategoryId(goodsSpu.getCategoryId());
            entity.setCategoryName(goodsCategoryService.getById(goodsSpu.getCategoryId()).getCategoryName());
            entity.setSkuCount(goodsNumBO.getSkuCount());
            entity.setStockCount(goodsNumBO.getStockCount());
            entity.setMaxPrice(goodsNumBO.getMaxPrice());
            entity.setMinPrice(goodsNumBO.getMinPrice());
            entity.setSkuId(goodsNumBO.getSkuId());
            entity.setIsHot(goodsSpu.getIsHot());
            entity.setIsNew(goodsSpu.getIsNew());
            entity.setSales(goodsSpu.getSales());
            entity.setSpuCode(goodsSpu.getSpuCode());

//            goodsSpuEsService.save(entity);

            goodsSpuEsService.updatePart(entity);
        }
    }

    @Test
    public void get2() throws IOException {
        com.baomidou.mybatisplus.core.metadata.IPage<GoodsSpuDoc> page = goodsSpuEsService.getAll("小米", 1, null, new PageReq(1, 10));
        System.out.println(page);

//        Page<GoodsSpuDoc> page1 = goodsSpuRepository.findByAllMatchesAndIsHotEquals("华为", 1, PageRequest.of(1, 10));
//        System.out.println(page1);
    }
}
