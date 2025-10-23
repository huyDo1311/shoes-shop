package com.cybersoft.shop.response.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class ProductListResponse {
    private List<ProductResponse> products;
    private Pagination pagination;

    @Data
    @Builder
    public static class Pagination {
        private int page;
        private int limit;
        private int page_size;
    }
}
