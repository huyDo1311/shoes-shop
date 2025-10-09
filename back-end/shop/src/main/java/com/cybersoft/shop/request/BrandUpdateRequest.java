package com.cybersoft.shop.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrandUpdateRequest {
    @Size(max = 50)
    private String brandName;
}
