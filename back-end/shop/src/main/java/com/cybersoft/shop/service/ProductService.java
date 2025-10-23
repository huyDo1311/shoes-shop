package com.cybersoft.shop.service;

import com.cybersoft.shop.entity.Product;
import com.cybersoft.shop.enums.SortType;
import com.cybersoft.shop.request.ProductRequest;
import com.cybersoft.shop.response.FilterCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface ProductService {
    Product createProduct (ProductRequest productRequest);
    boolean existsByProductName(String productName);

    Page<Product> filter(FilterCriteria criteria);

    Specification<Product> getProductSpecification(FilterCriteria criteria);

    Pageable createPageable(SortType sortType, int page, int sizePerPage);
}
