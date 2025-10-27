package com.cybersoft.shop.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;

@Getter
@Setter
public class SizeCreateRequest {
    @Min(value = 1, message = "Size value must be greater than 0")
    @NotNull(message = "Size value cannot be null")
    @UniqueElements(message = "Size value must be unique")
    private int sizeValue;
}
