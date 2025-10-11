package com.cybersoft.shop.response.size;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SizeResponse {
    private int id;
    private int sizeValue;
}
