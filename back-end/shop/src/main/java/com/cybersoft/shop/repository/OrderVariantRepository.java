package com.cybersoft.shop.repository;

import com.cybersoft.shop.entity.Order;
import com.cybersoft.shop.entity.OrderVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderVariantRepository extends JpaRepository<OrderVariant, Integer> {
//    Optional<OrderVariant> findByOrderAndVariantSku(Order order, String variantSku);
}
