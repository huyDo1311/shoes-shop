package com.cybersoft.shop.service;

import com.cybersoft.shop.request.BrandCreateRequest;
import com.cybersoft.shop.request.BrandUpdateRequest;
import com.cybersoft.shop.response.BrandResponse;

import java.util.List;

public interface BrandService {
    List<BrandResponse> listBrand();
    BrandResponse getBrandById(int id);
    BrandResponse createBrand(BrandCreateRequest createRequest);
    BrandResponse updateBrand(int id, BrandUpdateRequest updateRequest);
    void deleteBrand(int id);

}
