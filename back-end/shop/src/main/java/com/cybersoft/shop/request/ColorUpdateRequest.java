package com.cybersoft.shop.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColorUpdateRequest {
    @Size(max = 50, message = "Color name must not exceed 50 characters")
    private String colorName;
}
