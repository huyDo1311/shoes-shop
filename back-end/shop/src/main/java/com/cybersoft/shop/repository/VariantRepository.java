package com.cybersoft.shop.repository;

import com.cybersoft.shop.entity.Color;
import com.cybersoft.shop.entity.Product;
import com.cybersoft.shop.entity.Size;
import com.cybersoft.shop.entity.Variant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VariantRepository extends JpaRepository<Variant, String> {
    boolean existsByProductAndColorAndSize(Product product, Color color, Size size);
}
