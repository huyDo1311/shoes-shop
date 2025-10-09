package com.cybersoft.shop.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SizeResponse {
    private int id;
    private int sizeValue;
}
