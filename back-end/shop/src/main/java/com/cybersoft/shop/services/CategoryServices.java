package com.cybersoft.shop.services;

import com.cybersoft.shop.request.CategoryCreateRequest;
import com.cybersoft.shop.request.CategoryUpdateRequest;
import com.cybersoft.shop.response.CategoryResponse;

import java.util.List;

public interface CategoryServices {
    List<CategoryResponse> listCategory();
    CategoryResponse getCategoryById(int id);
    CategoryResponse createCategory(CategoryCreateRequest createRequest);
    CategoryResponse update(int id, CategoryUpdateRequest updateRequest);
    void delete(int id);
}
