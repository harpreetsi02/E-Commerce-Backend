package com.backend.ecommercebackend.mapper;

import com.backend.ecommercebackend.dto.request.ProductRequest;
import com.backend.ecommercebackend.dto.response.ProductResponse;
import com.backend.ecommercebackend.entity.Category;
import com.backend.ecommercebackend.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public static ProductResponse toResponse(Product product){

        ProductResponse response = new ProductResponse();

        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setStockQuantity(product.getStockQuantity());
        response.setCategoryName(product.getCategory().getName());

        return response;
    }

    public static Product toEntity(ProductRequest request, Category category){

        Product product = new Product();

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setCategory(category);

        return product;
    }
}
