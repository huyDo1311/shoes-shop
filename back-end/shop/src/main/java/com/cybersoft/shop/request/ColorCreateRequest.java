package com.cybersoft.shop.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColorCreateRequest {
    @NotBlank(message = "Color name is required")
    @Size(max = 50, message = "Color name must not exceed 50 characters")
    private String colorName;
}
