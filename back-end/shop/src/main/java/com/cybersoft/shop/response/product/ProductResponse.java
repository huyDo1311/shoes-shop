package com.cybersoft.shop.response.product;

import com.cybersoft.shop.entity.Product;
import com.cybersoft.shop.entity.ProductImage;
import com.cybersoft.shop.response.BaseResponse;
import com.cybersoft.shop.response.BrandResponse;
import com.cybersoft.shop.response.CategoryResponse;
import com.cybersoft.shop.response.variant.VariantResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponse extends BaseResponse {
    private int id;
    private String productName;
    private Float price;
    private String thumbnail;
    private String description;
    private CategoryResponse category;
    private BrandResponse brand;

    @JsonProperty("variant")
    private List<VariantResponse> variantResponses;
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
                .build();
        productResponse.setCreatedAt(product.getCreatedAt());
        productResponse.setUpdatedAt(product.getUpdatedAt());
        return productResponse;
    }


    public  static  ProductResponse toProductWithVariantDTO(Product product){

//        ProductResponse productDTO = ProductResponse.toDTO(product);
//
//        List<String> sizes = product.getVariants().stream()
//                .map(item -> String.valueOf(item.getSize().getSizeValue()))
//                .distinct()
//                .toList();
//        productDTO.setSize(sizes);
//
//        List<String> colors = product.getVariants().stream()
//                .map(item -> item.getColor().getColorName())
//                .distinct()
//                .toList();
//        productDTO.setColor(colors);

        // Tạo ProductResponse cơ bản
        ProductResponse productDTO = ProductResponse.toDTO(product);

        // Map danh sách variant
        List<VariantResponse> variantResponses = product.getVariants().stream()
                .map(VariantResponse::toDTO)
                .toList();

        productDTO.setVariantResponses(variantResponses);

        return productDTO;
    }

}
