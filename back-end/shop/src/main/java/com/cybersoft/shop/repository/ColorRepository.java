package com.cybersoft.shop.repository;

import com.cybersoft.shop.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorRepository extends JpaRepository<Color, Integer> {
    boolean existsByColorNameIgnoreCase(String colorName);
}
