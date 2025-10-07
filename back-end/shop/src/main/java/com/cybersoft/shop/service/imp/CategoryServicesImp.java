package com.cybersoft.shop.service.imp;

import com.cybersoft.shop.entity.Category;
import com.cybersoft.shop.repository.CategoryRepository;
import com.cybersoft.shop.request.CategoryCreateRequest;
import com.cybersoft.shop.request.CategoryUpdateRequest;
import com.cybersoft.shop.response.CategoryResponse;
import com.cybersoft.shop.service.CategoryServices;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class CategoryServicesImp implements CategoryServices {

    @Autowired
    private CategoryRepository categoryRepository;

    private CategoryResponse toRes(Category c){
        return CategoryResponse.builder()
                .id(c.getId())
                .categoryName(c.getCategoryName())
                .build();
    }

    @Override
    public List<CategoryResponse> listCategory() {
        return categoryRepository.findAll().stream().map(this::toRes).toList();
    }

    @Override
    public CategoryResponse getCategoryById(int id) {
        var c = categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Category not found"));
        return toRes(c);
    }

    @Override
    public CategoryResponse createCategory(CategoryCreateRequest createRequest) {
        if(categoryRepository.existsByCategoryNameIgnoreCase(createRequest.getCategoryName()))
            throw new IllegalArgumentException("Category name already exists");
        var c = Category.builder().categoryName(createRequest.getCategoryName().trim()).build();
        return toRes(categoryRepository.save(c));
    }

    @Override
    public CategoryResponse update(int id, CategoryUpdateRequest updateRequest) {
        var c = categoryRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Category not found"));
        if (updateRequest.getCategoryName() != null && !updateRequest.getCategoryName().isBlank()){
            if(categoryRepository.existsByCategoryNameIgnoreCase(updateRequest.getCategoryName()))
                throw new IllegalArgumentException("Category name already exists");
            c.setCategoryName(updateRequest.getCategoryName().trim());
        }
        return toRes(categoryRepository.save(c));
    }

    @Override
    public void delete(int id) {
        categoryRepository.deleteById(id);
    }
}
