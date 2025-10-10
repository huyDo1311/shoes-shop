package com.cybersoft.shop.request;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SizeUpdateRequest {
    @Min(value = 1, message = "Size value must be greater than 0")
    private int sizeValue;
}
