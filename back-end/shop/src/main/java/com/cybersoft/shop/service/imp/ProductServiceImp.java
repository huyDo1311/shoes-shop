package com.cybersoft.shop.service.imp;

import com.cybersoft.shop.entity.Brand;
import com.cybersoft.shop.entity.Category;
import com.cybersoft.shop.entity.Product;
import com.cybersoft.shop.entity.ProductImage;
import com.cybersoft.shop.enums.SortType;
import com.cybersoft.shop.repository.BrandRepository;
import com.cybersoft.shop.repository.CategoryRepository;
import com.cybersoft.shop.repository.ProductRepository;
import com.cybersoft.shop.request.ProductCreateRequest;
import com.cybersoft.shop.request.ProductUpdateRequest;
import com.cybersoft.shop.response.FilterCriteria;
import com.cybersoft.shop.response.product.ProductResponse;
import com.cybersoft.shop.service.ProductService;
import com.cybersoft.shop.specification.ProductSpecification;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
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
    public Product createProduct(ProductCreateRequest productCreateRequest) {
        Category existingCategory = categoryRepository.findById(productCreateRequest.getCategoryId()).orElseThrow(
                ()-> new EntityNotFoundException("Not found")
        );
        Brand existingBrand = brandRepository.findById(productCreateRequest.getBrandId()).orElseThrow(
                ()-> new EntityNotFoundException("Not found")
        );
        Product newProduct = Product.builder()
                .productName(productCreateRequest.getProductName())
                .description(productCreateRequest.getDescription())
                .price(productCreateRequest.getPrice())
                .thumbnail(productCreateRequest.getThumbnail())
                .brand(existingBrand)
                .category(existingCategory)
                .build();

        if (productCreateRequest.getProductImages() != null && !productCreateRequest.getProductImages().isEmpty()) {
            List<ProductImage> images = productCreateRequest.getProductImages().stream()
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

    @Transactional
    @Override
    public Product updateProduct(int id, ProductUpdateRequest req) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        if (req.getProductName() != null) {
            String name = req.getProductName().trim();
            if (!name.equalsIgnoreCase(existing.getProductName())
                    && productRepository.existsByProductName(name)) {
                throw new ValidationException("Product name already exists");
            }
            existing.setProductName(name);
        }

        if (req.getPrice() != null) {
            existing.setPrice(req.getPrice());
        }

        if (req.getThumbnail() != null) {
            existing.setThumbnail(req.getThumbnail().trim());
        }

        if (req.getDescription() != null) {
            existing.setDescription(req.getDescription().trim());
        }

        if (req.getCategoryId() != null) {
            var cat = categoryRepository.findById(req.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found"));
            existing.setCategory(cat);
        }

        if (req.getBrandId() != null) {
            var brand = brandRepository.findById(req.getBrandId())
                    .orElseThrow(() -> new EntityNotFoundException("Brand not found"));
            existing.setBrand(brand);
        }

        if (req.getProductImages() != null) {
            List<ProductImage> images = new ArrayList<>();
            for (ProductImage img : req.getProductImages()) {
                ProductImage copy = ProductImage.builder()
                        .id(img.getId())
                        .imageUrl(img.getImageUrl())
                        .product(existing)
                        .build();
                images.add(copy);
            }
            existing.setProductImages(images);
        }

        return productRepository.save(existing);
    }

    @Override
    public void deleteProduct(int id) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        productRepository.delete(existing);
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

    @Override
    public ResponseEntity<?> getBySearch(String name) {
        Specification<Product> spec = ProductSpecification.hasKeyword(name);

        List<ProductResponse> products = productRepository.findAll(spec)
                .stream().map(ProductResponse::toDTO).toList();

        return  ResponseEntity.ok(products);
    }

    @Override
    public Product getById(int id) {
        var existing = productRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Not Found")
        );

        return existing;
    }

}
