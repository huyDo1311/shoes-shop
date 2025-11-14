package com.cybersoft.shop.controller;

import com.cybersoft.shop.entity.Product;
import com.cybersoft.shop.enums.SortType;
import com.cybersoft.shop.request.ProductCreateRequest;
import com.cybersoft.shop.request.ProductUpdateRequest;
import com.cybersoft.shop.response.FilterCriteria;
import com.cybersoft.shop.response.ResponseObject;
import com.cybersoft.shop.response.product.ProductListResponse;
import com.cybersoft.shop.response.product.ProductResponse;
import com.cybersoft.shop.service.ProductService;
import com.github.javafaker.Faker;
import jakarta.validation.Valid;
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
    public ResponseEntity<?> createProduct(@RequestBody ProductCreateRequest productCreateRequest){
        Product newProduct = productService.createProduct(productCreateRequest);
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

    @GetMapping("/search")
    public  ResponseEntity<?> getSearch(@RequestParam(value = "name") String name  ){
        return  productService.getBySearch(name);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Integer id){
        Product product = productService.getById(id);
        ProductResponse productResponse = ProductResponse.toProductWithVariantDTO(product);

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Find product successfully")
                        .status(200)
                        .data(productResponse)
                        .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id") int id,
                                           @Valid @RequestBody ProductUpdateRequest req) {
        ProductResponse updated = productService.updateProduct(id, req);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Update product successfully")
                        .status(HttpStatus.OK.value())
                        .data(updated)
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") int id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Delete product successfully")
                        .status(HttpStatus.OK.value())
                        .data(null)
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
            ProductCreateRequest productCreateRequest = ProductCreateRequest.builder()
                    .productName(productName)
                    .price((float)faker.number().numberBetween(100_000, 2_000_000))
                    .description(faker.lorem().sentence())
                    .categoryId(faker.number().numberBetween(1,5))
                    .brandId(faker.number().numberBetween(1,5))
                    .build();
            try {
                productService.createProduct(productCreateRequest);
            }catch (Exception e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.ok("Fake products created successfully");
    }

}
