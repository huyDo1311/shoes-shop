package com.cybersoft.shop.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class BaseResponse {
    private int code;
    private String message;
    private Object data;
}
