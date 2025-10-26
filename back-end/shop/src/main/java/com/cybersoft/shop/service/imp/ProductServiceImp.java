package com.cybersoft.shop.service.imp;

import com.cybersoft.shop.entity.Brand;
import com.cybersoft.shop.entity.Category;
import com.cybersoft.shop.entity.Product;
import com.cybersoft.shop.entity.ProductImage;
import com.cybersoft.shop.enums.SortType;
import com.cybersoft.shop.repository.BrandRepository;
import com.cybersoft.shop.repository.CategoryRepository;
import com.cybersoft.shop.repository.ProductRepository;
import com.cybersoft.shop.request.ProductRequest;
import com.cybersoft.shop.response.FilterCriteria;
import com.cybersoft.shop.service.ProductService;
import com.cybersoft.shop.specification.ProductSpecification;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
public class ProductServiceImp implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BrandRepository brandRepository;

    @Override
    public Product createProduct(ProductRequest productRequest) {
        Category existingCategory = categoryRepository.findById(productRequest.getCategoryId()).orElseThrow(
                ()-> new EntityNotFoundException("Not found")
        );
        Brand existingBrand = brandRepository.findById(productRequest.getBrandId()).orElseThrow(
                ()-> new EntityNotFoundException("Not found")
        );
        Product newProduct = Product.builder()
                .productName(productRequest.getProductName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .thumbnail(productRequest.getThumbnail())
                .brand(existingBrand)
                .category(existingCategory)
                .build();

        if (productRequest.getProductImages() != null && !productRequest.getProductImages().isEmpty()) {
            List<ProductImage> images = productRequest.getProductImages().stream()
                    .map(imgReq -> {
                        ProductImage img = new ProductImage();
                        img.setImageUrl(imgReq.getImageUrl());
                        img.setProduct(newProduct);
                        return img;
                    })
                    .toList();

            newProduct.setProductImages(images);
        }
        return productRepository.save(newProduct);
    }

    @Override
    public boolean existsByProductName(String productName) {
        return productRepository.existsByProductName(productName);
    }

    @Override
    public Page<Product> filter(FilterCriteria criteria) {
        Pageable pageable = createPageable(criteria.getSort(), criteria.getPage(), criteria.getLimit());
        Specification<Product> spec = getProductSpecification(criteria);
        Page<Product> products = productRepository.findAll(spec, pageable);
        return productRepository.findAll(spec, pageable);
    }

    @Override
    public Specification<Product> getProductSpecification(FilterCriteria criteria) {
        return Specification.allOf(
                ProductSpecification.hasCategory(criteria.getCategory()),
                ProductSpecification.hasBrand(criteria.getBrand()),
                ProductSpecification.hasKeyword(criteria.getKeyword()),
                ProductSpecification.hasPriceRange(criteria.getMinPrice(), criteria.getMaxPrice()),
                ProductSpecification.hasSizes(criteria.getSize()),
                ProductSpecification.hasColor(criteria.getColor())
        );
    }

    @Override
    public Pageable createPageable(SortType sortType, int page, int sizePerPage) {
        Sort sort = (sortType != null) ? switch (sortType) {
            case Newest -> Sort.by(Sort.Direction.DESC, "createdAt");
            case PriceDESC -> Sort.by(Sort.Direction.DESC, "price");
            case PriceASC -> Sort.by(Sort.Direction.ASC, "price");
        } : Sort.unsorted();

        int offSet = page*sizePerPage;

        return PageRequest.of(page, sizePerPage,sort);
    }
}
