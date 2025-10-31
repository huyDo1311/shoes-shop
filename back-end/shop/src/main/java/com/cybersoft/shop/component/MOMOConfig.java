package com.cybersoft.shop.component;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class MOMOConfig {

    @Value("${momo.end-point}")
    private String momoUrl;

    @Value("${momo.return-url}")
    private String momoReturnUrl;

    @Value("${momo.ipn-url}")
    private String ipnUrl;

    @Value("${momo.partner-code}")
    private String partnerCode;

    @Value("${momo.access-key}")
    private String accessKey;

    @Value("${momo.secret-key}")
    private String secretKey;

}

