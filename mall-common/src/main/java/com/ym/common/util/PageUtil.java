package com.ym.common.util;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.List;

/**
 * @author qushutao
 * @since 2026-06-29 14:44
 **/
public class PageUtil {

    private PageUtil() {
    }

    public static <T,K> IPage<T> getPage(IPage<K> pageReq, List<T> resultList) {
        IPage<T> result = new Page<>(pageReq.getCurrent(), pageReq.getSize(), pageReq.getTotal());
        result.setRecords(resultList);
        return result;
    }

    public static <T> IPage<T> getPagByParma(Integer current, Integer size, Integer total, List<T> resultList) {
        IPage<T> result = new Page<>(current, size, total);
        result.setRecords(resultList);
        return result;
    }
}
