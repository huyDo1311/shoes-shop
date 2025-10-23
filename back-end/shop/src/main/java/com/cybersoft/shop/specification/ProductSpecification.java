package com.cybersoft.shop.specification;
import com.cybersoft.shop.entity.Product;
import com.cybersoft.shop.entity.Variant;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ProductSpecification {

    public static Specification<Product> hasCategory(String categoryId) {
        return (root, query, cb) -> {
            if (categoryId == null || categoryId.equalsIgnoreCase("all")) {
                return cb.conjunction();
            }
            return root.get("category").get("id").in(Long.valueOf(categoryId));
        };
    }

    public static Specification<Product> hasBrand(String brandId) {
        return (root, query, cb) -> {
            if (brandId == null || brandId.equalsIgnoreCase("all")) {
                return cb.conjunction();
            }
            return root.get("brand").get("id").in(Long.valueOf(brandId));
        };
    }

    public static Specification<Product> hasKeyword(String keyword) {
        return (root, query, cb) -> keyword == null ? cb.conjunction() : cb.like(root.get("productName"), "%" + keyword + "%");
    }

    public static Specification<Product> hasPriceRange(Double minPrice, Double maxPrice) {
        return (root, query, cb) -> {
            Predicate minPricePredicate = minPrice == null ? cb.conjunction() : cb.ge(root.get("price"), minPrice);
            Predicate maxPricePredicate = maxPrice == null ? cb.conjunction() : cb.le(root.get("price"), maxPrice);
            return cb.and(minPricePredicate, maxPricePredicate);
        };
    }

    public static Specification<Product> hasColor(List<Long> colorIds) {
        return (root, query, cb) -> {
            if (colorIds == null || colorIds.isEmpty()) {
                return cb.conjunction();
            }

            Join<Product, Variant> variantJoin = root.join("variants");
            Predicate predicate = variantJoin.get("color").get("id").in(colorIds);
            query.distinct(true); // tránh trùng product khi có nhiều variants
            return predicate;
        };
    }

    public static Specification<Product> hasSizes(List<String> sizes) {
        return (root, query, cb) -> {
            if (sizes == null || sizes.isEmpty()) {
                return cb.conjunction();
            }

            Join<Product, Variant> variantJoin = root.join("variants");
            Predicate predicate = variantJoin.get("size").get("value").in(sizes);
            query.distinct(true);
            return predicate;
        };
    }


}
