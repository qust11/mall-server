package com.ym.product.dto.resp.category;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qushutao
 * @since 2026-06-30 20:12
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PrimaryCategoryResp {

    private Long id;

    private String categoryName;
}
