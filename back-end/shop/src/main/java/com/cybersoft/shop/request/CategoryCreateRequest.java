package com.cybersoft.shop.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CategoryCreateRequest {
    @NotBlank
    @Size(max = 100)
    private String categoryName;
}
