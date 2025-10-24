package com.cybersoft.shop.response;

import com.cybersoft.shop.entity.Color;
import com.cybersoft.shop.entity.Size;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ColorResponse {

    private int id;

    private String colorName;

    public static ColorResponse toDTO(Color color){
        return  ColorResponse.builder()
                .id(color.getId())
                .colorName(color.getColorName())
                .build();
    }
}
