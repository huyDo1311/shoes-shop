package com.cybersoft.shop.request;

import com.cybersoft.shop.entity.ProductImage;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductUpdateRequest {
    @Size(min = 2, max = 200, message = "Product name must be between 2 and 200 characters")
    @JsonProperty("product_name")
    private String productName;

    @PositiveOrZero(message = "Price must be zero or positive")
    private Float price;

    @Size(max = 2000, message = "Description length must be <= 2000")
    private String description;

    @Positive(message = "categoryId must be a positive integer")
    private Integer categoryId;

    @Positive(message = "brandId must be a positive integer")
    private Integer brandId;

    private String thumbnail;

    private List<ProductImage> productImages;
}
