package com.cybersoft.shop.mapper;

import com.cybersoft.shop.entity.Category;
import com.cybersoft.shop.request.CategoryCreateRequest;
import com.cybersoft.shop.request.CategoryUpdateRequest;
import com.cybersoft.shop.response.category.CategoryResponse;

public class CategoryMapper {

    public static CategoryResponse toResponse(Category c){
        return CategoryResponse.builder()
                .id(c.getId())
                .categoryName(c.getCategoryName())
                .build();
    }

    public static Category toEntity(CategoryCreateRequest req){
        return Category.builder()
                .categoryName(req.getCategoryName().trim())
                .build();
    }

    public static void updateEntity(Category c, CategoryUpdateRequest req){
        if(req.getCategoryName() != null && !req.getCategoryName().isBlank()){
            c.setCategoryName(req.getCategoryName().trim());
        }
    }
}
