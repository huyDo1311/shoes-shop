package com.cybersoft.shop.response.product;

import com.cybersoft.shop.entity.Brand;
import com.cybersoft.shop.entity.Category;
import com.cybersoft.shop.entity.Product;
import com.cybersoft.shop.entity.ProductImage;
import com.cybersoft.shop.response.BaseResponse;
import com.cybersoft.shop.response.BrandResponse;
import com.cybersoft.shop.response.CategoryResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse extends BaseResponse {
    private int id;
    private String productName;
    private Float price;
    private String thumbnail;
    private String description;
    private int categoryId;
    private CategoryResponse category;
    private int brandId;
    private BrandResponse brand;
//    private List<VariantDTO> variantDTOS;

    @JsonProperty("product_images")
    private List<ProductImage> productImages = new ArrayList<>();

    public static ProductResponse toDTO(Product product) {
        ProductResponse productResponse = ProductResponse.builder()
                .productName(product.getProductName())
                .id(product.getId())
                .description(product.getDescription())
                .thumbnail(product.getThumbnail())
                .productImages(product.getProductImages())
                .category(CategoryResponse.toDTO(product.getCategory()))
                .price(product.getPrice())
                .brand(BrandResponse.toDTO(product.getBrand()))
                .categoryId(product.getCategory().getId())
                .brandId(product.getBrand().getId())
                .build();
        productResponse.setCreatedAt(product.getCreatedAt());
        productResponse.setUpdatedAt(product.getUpdatedAt());
        return productResponse;
    }


}
