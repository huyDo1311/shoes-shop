package com.cybersoft.shop.controller;

import com.cybersoft.shop.request.CategoryCreateRequest;
import com.cybersoft.shop.request.CategoryUpdateRequest;
import com.cybersoft.shop.response.BaseResponse;
import com.cybersoft.shop.response.CategoryResponse;
import com.cybersoft.shop.services.CategoryServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryServices categoryServices;

    @GetMapping
    public ResponseEntity<?> getAllCategory(){

        List<CategoryResponse> data = categoryServices.listCategory();
        BaseResponse res = new BaseResponse();
        res.setCode(HttpStatus.OK.value());
        res.setMessage("Get categories successfully");
        res.setData(data);

        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable int id){
        CategoryResponse data = categoryServices.getCategoryById(id);
        BaseResponse res = new BaseResponse();
        res.setCode(HttpStatus.OK.value());
        res.setMessage("Get category successfully");
        res.setData(data);

        return ResponseEntity.ok(res);
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryCreateRequest req){
        CategoryResponse data = categoryServices.createCategory(req);

        BaseResponse res = new BaseResponse();
        res.setCode(HttpStatus.CREATED.value());
        res.setMessage("Create category successfully");
        res.setData(data);

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable int id, @Valid @RequestBody CategoryUpdateRequest req){
        CategoryResponse data = categoryServices.update(id, req);

        BaseResponse res = new BaseResponse();
        res.setCode(HttpStatus.OK.value());
        res.setMessage("Update category successfully");
        res.setData(data);

        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable int id){
        categoryServices.delete(id);

        BaseResponse res =  new BaseResponse();
        res.setCode(HttpStatus.OK.value());
        res.setMessage("Delete category successfully");
        res.setData(null);

        return ResponseEntity.ok(res);
    }



}
