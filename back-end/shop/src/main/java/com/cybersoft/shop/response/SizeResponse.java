package com.cybersoft.shop.response;

import com.cybersoft.shop.entity.Category;
import com.cybersoft.shop.entity.Size;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SizeResponse {

    private int id;
    private int sizeValue;

    public static SizeResponse toDTO(Size size){
        return  SizeResponse.builder()
                .id(size.getId())
                .sizeValue(size.getSizeValue())
                .build();
    }
}
