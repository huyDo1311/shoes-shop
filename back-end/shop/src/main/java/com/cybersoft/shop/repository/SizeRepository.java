package com.cybersoft.shop.repository;

import com.cybersoft.shop.entity.Sizes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SizeRepository extends JpaRepository<Sizes, Integer> {
}
