package com.cybersoft.shop.service.imp;

import com.cybersoft.shop.entity.Variant;
import com.cybersoft.shop.repository.ColorRepository;
import com.cybersoft.shop.repository.ProductRepository;
import com.cybersoft.shop.repository.SizeRepository;
import com.cybersoft.shop.repository.VariantRepository;
import com.cybersoft.shop.request.VariantRequest;
import com.cybersoft.shop.service.ProductService;
import com.cybersoft.shop.service.VariantService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
public class VariantServiceImp implements VariantService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private VariantRepository variantRepository;
    @Autowired
    private ColorRepository colorRepository;
    @Autowired
    private SizeRepository sizeRepository;

    @Override
    public Variant createVariant(VariantRequest variantRequest) {

        var product = productRepository.findById(variantRequest.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        var color = colorRepository.findById(variantRequest.getColorId())
                .orElseThrow(() -> new EntityNotFoundException("Color not found"));

        var size = sizeRepository.findById(variantRequest.getSizeId())
                .orElseThrow(() -> new EntityNotFoundException("Size not found"));

        boolean exists = variantRepository.existsByProductAndColorAndSize(product, color, size);
        if (exists) {
            throw new DuplicateKeyException("This variant already exists for this product");
        }

        Variant variant = new Variant();
        variant.setQuantity(variantRequest.getQuantity());
        variant.setProduct(product);
        variant.setColor(color);
        variant.setSize(size);

        return variantRepository.save(variant);
    }

}
