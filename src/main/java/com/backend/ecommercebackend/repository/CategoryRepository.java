package com.backend.ecommercebackend.repository;

import com.backend.ecommercebackend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
