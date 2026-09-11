package com.backend.ecommercebackend.mapper;

import com.backend.ecommercebackend.dto.request.CategoryRequest;
import com.backend.ecommercebackend.dto.response.CategoryResponse;
import com.backend.ecommercebackend.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryResponse toResponse(Category category){

        CategoryResponse response = new CategoryResponse();

        response.setId(category.getId());
        response.setName(category.getName());
        response.setCreatedAt(category.getCreatedAt());

        return response;
    }

    public Category toEntity(CategoryRequest request){

        Category category = new Category();

        category.setName(request.getName());

        return category;
    }
}
