package com.cybersoft.shop.service;

import com.cybersoft.shop.entity.Variant;
import com.cybersoft.shop.request.VariantRequest;
import com.cybersoft.shop.response.variant.VariantResponse;

public interface VariantService {
    VariantResponse createVariant (VariantRequest variantRequest);

    VariantResponse getVariantById(String id);

    VariantResponse updateVariant(String sku, VariantRequest req);

    void deleteVariant(String sku);
}
