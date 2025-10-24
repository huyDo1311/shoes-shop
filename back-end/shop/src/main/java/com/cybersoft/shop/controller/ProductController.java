package com.cybersoft.shop.controller;

import com.cybersoft.shop.entity.Product;
import com.cybersoft.shop.enums.SortType;
import com.cybersoft.shop.request.ProductRequest;
import com.cybersoft.shop.response.FilterCriteria;
import com.cybersoft.shop.response.ResponseObject;
import com.cybersoft.shop.response.product.ProductListResponse;
import com.cybersoft.shop.response.product.ProductResponse;
import com.cybersoft.shop.service.ProductService;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductRequest productRequest){
        Product newProduct = productService.createProduct(productRequest);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Create new product successfully")
                        .status(HttpStatus.CREATED.value())
                        .data(newProduct)
                        .build());
    }

    @GetMapping
    public ResponseEntity<?> getProducts(
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "brand", required = false) String brand,
            @RequestParam(value = "color", required = false) List<Integer> color,
            @RequestParam(value = "size", required = false) List<Integer> size,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "minPrice", required = false) Double minPrice,
            @RequestParam(value = "maxPrice", required = false) Double maxPrice,
            @RequestParam(value = "sort", required = false) SortType sortType,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "6") int sizePerPage
    ) {

        FilterCriteria filterCriteria = FilterCriteria.builder()
                .category(category)
                .size(size)
                .page(page)
                .limit(sizePerPage)
                .brand(brand)
                .color(color)
                .keyword(keyword)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .sort(sortType)
                .build();

        Page<Product> productsPage = productService.filter(filterCriteria);

        List<ProductResponse> productResponses = productsPage
                .getContent()
                .stream()
                .map(ProductResponse::toProductWithVariantDTO)
                .toList();

        ProductListResponse productListResponse = ProductListResponse.builder()
                .products(productResponses)
                .pagination(ProductListResponse.Pagination.builder()
                        .page(page + 1)
                        .limit(sizePerPage)
                        .page_size(productsPage.getTotalPages())
                        .build())
                .build();

        return ResponseEntity.ok(ResponseObject.builder()
                .message("Get products successfully")
                .status(HttpStatus.OK.value())
                .data(productListResponse)
                .build());
    }

    @PostMapping("/generateFakeLikes")
    public ResponseEntity<String> generateFakeLikes() {
        Faker faker = new Faker();
        for(int i=0; i<1000; i++){
            String productName = faker.commerce().productName();
            if(productService.existsByProductName(productName)){
                continue;
            }
            ProductRequest productRequest = ProductRequest.builder()
                    .productName(productName)
                    .price((float)faker.number().numberBetween(100_000, 2_000_000))
                    .description(faker.lorem().sentence())
                    .categoryId(faker.number().numberBetween(1,5))
                    .brandId(faker.number().numberBetween(1,5))
                    .build();
            try {
                productService.createProduct(productRequest);
            }catch (Exception e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.ok("Fake products created successfully");
    }

}
