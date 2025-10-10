package com.cybersoft.shop.controller;

import com.cybersoft.shop.request.BrandCreateRequest;
import com.cybersoft.shop.request.BrandUpdateRequest;
import com.cybersoft.shop.response.BrandResponse;
import com.cybersoft.shop.response.ResponseObject;
import com.cybersoft.shop.service.BrandService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brands")
@Validated
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping
    public ResponseEntity<ResponseObject> getAllBrand(){
        List<BrandResponse> data = brandService.listBrand();

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .status(HttpStatus.OK.value())
                        .message("Get all brands successfully")
                        .data(data)
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getBrandById(@PathVariable int id){
        BrandResponse data = brandService.getBrandById(id);

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .status(HttpStatus.OK.value())
                        .message("Get brands successfully")
                        .data(data)
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<ResponseObject> createBrand(@Valid @RequestBody BrandCreateRequest req){
        BrandResponse data = brandService.createBrand(req);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ResponseObject.builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Create brands successfully")
                        .data(data)
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject>  updateBrand(
            @PathVariable int id, @Valid @RequestBody BrandUpdateRequest req){
        BrandResponse data = brandService.updateBrand(id, req);

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .status(HttpStatus.OK.value())
                        .message("Update brand successfully")
                        .data(data)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteBrand(@PathVariable int id){
        brandService.deleteBrand(id);

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .status(HttpStatus.OK.value())
                        .message("Delete brand successfully")
                        .data(null)
                        .build()
        );
    }
}
