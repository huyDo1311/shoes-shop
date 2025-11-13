package com.cybersoft.shop.controller;

import com.cybersoft.shop.request.VariantRequest;
import com.cybersoft.shop.response.ResponseObject;
import com.cybersoft.shop.response.variant.VariantResponse;
import com.cybersoft.shop.service.VariantService;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/variants")
public class VariantController {
    @Autowired
    private VariantService variantService;

    @PostMapping
    public ResponseEntity<?> createVariant(@RequestBody VariantRequest variantRequest){
        VariantResponse newVariant = variantService.createVariant(variantRequest);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Create new variant successfully")
                        .status(HttpStatus.CREATED.value())
                        .data(newVariant)
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVariant(@PathVariable("id") String id){
        VariantResponse getVariant = variantService.getVariantById(id);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Get variant successfully")
                        .status(HttpStatus.OK.value())
                        .data(getVariant)
                        .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateVariant(@PathVariable("id") String id,
                                           @RequestBody VariantRequest variantRequest){
        VariantResponse updateVariant = variantService.updateVariant(id,variantRequest);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Update variant successfully")
                        .status(HttpStatus.OK.value())
                        .data(updateVariant)
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVariant(@PathVariable("id") String id){
        variantService.deleteVariant(id);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Delete variant successfully")
                        .status(HttpStatus.OK.value())
                        .data(null)
                        .build());
    }

    @PostMapping("/generateFakeData")
    public ResponseEntity<String> generateFakeData() {
        Faker faker = new Faker();

        int createdCount = 0;
        int skippedCount = 0;

        int totalProducts = 1000;
        int totalColors = 5;
        int totalSizes = 5;

        for (int i = 0; i < 2000; i++) { // tạo 200 variants
            int productId = faker.number().numberBetween(1, totalProducts + 1);
            int colorId = faker.number().numberBetween(1, totalColors + 1);
            int sizeId = faker.number().numberBetween(1, totalSizes + 1);
            int quantity = faker.number().numberBetween(1, 100);
            VariantRequest variantRequest = VariantRequest.builder()
                    .productId(productId)
                    .colorId(colorId)
                    .sizeId(sizeId)
                    .quantity(quantity)
                    .build();
            try {
                variantService.createVariant(variantRequest);
                createdCount++;
            } catch (Exception e) {
                // Có thể trùng khóa hoặc lỗi khóa ngoại
                skippedCount++;
            }
        }
        String message = String.format(
                "Fake variants created successfully: %d created, %d skipped due to duplicates or FK errors.",
                createdCount, skippedCount
        );
        return ResponseEntity.ok(message);
    }
}
