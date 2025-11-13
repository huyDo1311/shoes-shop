package com.cybersoft.shop.repository;

import com.cybersoft.shop.entity.Color;
import com.cybersoft.shop.entity.Product;
import com.cybersoft.shop.entity.Size;
import com.cybersoft.shop.entity.Variant;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface VariantRepository extends JpaRepository<Variant, String> {
    boolean existsByProductAndColorAndSize(Product product, Color color, Size size);

    @EntityGraph(attributePaths = {"product","color","size"})
    Optional<Variant> findBySku(String sku);

    @EntityGraph(attributePaths = {"product", "color", "size"})
    List<Variant> findBySkuIn(Collection<String> skus);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select v from Variant v where v.sku in :skus")
    List<Variant> findBySkuInForUpdate(@Param("skus") Collection<String> skus);


}
