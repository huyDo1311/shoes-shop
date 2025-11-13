package com.cybersoft.shop.service.imp;

import com.cybersoft.shop.entity.Variant;
import com.cybersoft.shop.repository.ColorRepository;
import com.cybersoft.shop.repository.ProductRepository;
import com.cybersoft.shop.repository.SizeRepository;
import com.cybersoft.shop.repository.VariantRepository;
import com.cybersoft.shop.request.VariantRequest;
import com.cybersoft.shop.response.variant.VariantResponse;
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
    public VariantResponse createVariant(VariantRequest variantRequest) {

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

        Variant saved = variantRepository.save(variant);
        return VariantResponse.toDTO(saved);
    }

    @Override
    public VariantResponse getVariantById(String id) {
        Variant variant = variantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Variant not found"));

        return VariantResponse.toDTO(variant);
    }

    @Override
    public VariantResponse updateVariant(String id, VariantRequest req) {

        Variant existing = variantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Variant not found"));

        if (req.getProductId() != 0) {
            var product = productRepository.findById(req.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found"));
            existing.setProduct(product);
        }


        if (req.getColorId() != 0) {
            var color = colorRepository.findById(req.getColorId())
                    .orElseThrow(() -> new EntityNotFoundException("Color not found"));
            existing.setColor(color);
        }

        if (req.getSizeId() != 0) {
            var size = sizeRepository.findById(req.getSizeId())
                    .orElseThrow(() -> new EntityNotFoundException("Size not found"));
            existing.setSize(size);
        }


        if (req.getQuantity() != 0) {
            existing.setQuantity(req.getQuantity());
        }

        boolean exists = variantRepository.existsByProductAndColorAndSize(
                existing.getProduct(),
                existing.getColor(),
                existing.getSize()
        );

        boolean same = variantRepository
                .existsByProductAndColorAndSize(existing.getProduct(), existing.getColor(), existing.getSize());

        if (same) {
            throw new DuplicateKeyException("This variant already exists for this product");
        }

        Variant saved = variantRepository.save(existing);
        return VariantResponse.toDTO(saved);
    }

    @Override
    public void deleteVariant(String id) {
        Variant existing = variantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Variant not found"));

        variantRepository.delete(existing);
    }



}
