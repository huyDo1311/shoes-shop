package com.cybersoft.shop.service;

import com.cybersoft.shop.entity.Product;
import com.cybersoft.shop.entity.Variant;
import com.cybersoft.shop.request.ProductRequest;
import com.cybersoft.shop.request.VariantRequest;

public interface VariantService {
    Variant createVariant (VariantRequest variantRequest);
}
