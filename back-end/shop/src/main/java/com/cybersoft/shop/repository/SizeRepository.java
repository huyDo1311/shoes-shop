package com.cybersoft.shop.repository;

import com.cybersoft.shop.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SizeRepository extends JpaRepository<Size, Integer> {
    boolean existsBySizeValue(int sizeValue);
}
