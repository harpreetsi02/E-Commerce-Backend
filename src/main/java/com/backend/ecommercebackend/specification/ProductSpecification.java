package com.backend.ecommercebackend.specification;

import com.backend.ecommercebackend.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecification {

    public static Specification<Product> hasName(String name){

        return (root, query, criteriaBuilder) -> {

            if (name == null || name.isBlank()) return criteriaBuilder.conjunction();
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("name")),
                    "%" + name.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Product> hasCategoryId(Long categoryId){

        return (root, query, criteriaBuilder) -> {

            if (categoryId == null) return criteriaBuilder.conjunction();
            return criteriaBuilder.equal(root.get("category").get("id"), categoryId);
        };
    }

    public static Specification<Product> priceGreaterThanOrEqual(BigDecimal minPrice){

        return (root, query, criteriaBuilder) -> {

            if (minPrice == null) return criteriaBuilder.conjunction();
            return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
        };
    }

    public static Specification<Product> priceLessThanEqual(BigDecimal maxPrice){

        return (root, query, criteriaBuilder) -> {

            if (maxPrice == null) return criteriaBuilder.conjunction();
            return criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
        };
    }
}
