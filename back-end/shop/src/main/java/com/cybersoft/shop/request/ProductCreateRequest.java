package com.cybersoft.shop.request;

import com.cybersoft.shop.entity.ProductImage;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.List;

@Data//toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductCreateRequest {
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 200, message = "Name must be between 3 and 200 characters")
    private String productName;

    @Min(value = 0, message = "Price must be greater than or equal to 0")
    @Max(value = 10000000, message = "Price must be less than or equal to 10,000,000")
    private Float price;

    private String thumbnail;

    @Size(max = 2000, message = "Description length must be <= 2000")
    private String description;
    
    private int categoryId;

    private int brandId;

    private List<ProductImage> productImages;
}
