package com.cybersoft.shop.repository;

import com.cybersoft.shop.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsByCategoryNameIgnoreCase(String categoryName);
}
