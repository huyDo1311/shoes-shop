package com.cybersoft.shop.service.imp;

import com.cybersoft.shop.entity.Brand;
import com.cybersoft.shop.repository.BrandRepository;
import com.cybersoft.shop.request.BrandCreateRequest;
import com.cybersoft.shop.request.BrandUpdateRequest;
import com.cybersoft.shop.response.BrandResponse;
import com.cybersoft.shop.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImp implements BrandService {

    @Autowired
    private BrandRepository brandRepository;

    private BrandResponse toRes(Brand b){
        return BrandResponse.builder()
                .id(b.getId())
                .brandName(b.getBrandName())
                .build();
    }
    @Override
    public List<BrandResponse> listBrand() {
        return brandRepository.findAll().stream().map(this::toRes).toList();
    }

    @Override
    public BrandResponse getBrandById(int id) {
        var b = brandRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Brand not found"));
        return toRes(b);
    }

    @Override
    public BrandResponse createBrand(BrandCreateRequest createRequest) {
        if(brandRepository.existsByBrandNameIgnoreCase(createRequest.getBrandName()))
            throw new IllegalArgumentException("Brand name already exists");
        var b = Brand.builder().brandName(createRequest.getBrandName().trim()).build();
        return toRes(brandRepository.save(b));
    }

    @Override
    public BrandResponse updateBrand(int id, BrandUpdateRequest updateRequest) {
        var b = brandRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Brand not found"));
        if(updateRequest.getBrandName() != null && !updateRequest.getBrandName().isBlank()) {
            if (brandRepository.existsByBrandNameIgnoreCase(updateRequest.getBrandName()))
                throw new IllegalArgumentException("Brand name already exists");
            b.setBrandName(updateRequest.getBrandName().trim());
        }
        return toRes(brandRepository.save(b));
    }

    @Override
    public void deleteBrand(int id) {
        brandRepository.deleteById(id);

    }
}

