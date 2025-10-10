package com.cybersoft.shop.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ColorResponse {
    private int id;
    private String colorName;
}
