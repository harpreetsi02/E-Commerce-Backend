package com.backend.ecommercebackend.repository;

import com.backend.ecommercebackend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>,
        JpaSpecificationExecutor<Product> {

    @Query("SELECT p FROM Product p JOIN FETCH p.category")
    List<Product> getAllWithCategory();

}
