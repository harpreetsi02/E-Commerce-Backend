package com.backend.ecommercebackend.service;

import com.backend.ecommercebackend.dto.request.CategoryRequest;
import com.backend.ecommercebackend.dto.response.CategoryResponse;
import com.backend.ecommercebackend.entity.Category;
import com.backend.ecommercebackend.exception.CategoryNotFoundException;
import com.backend.ecommercebackend.mapper.CategoryMapper;
import com.backend.ecommercebackend.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(
            CategoryRepository categoryRepository,
            CategoryMapper categoryMapper
    ) {
        this.categoryMapper = categoryMapper;
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponse createCategory(CategoryRequest request){

        Category category = categoryMapper.toEntity(request);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toResponse(savedCategory);
    }

    public CategoryResponse getCategoryById(Long id){

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new CategoryNotFoundException(
                                "Category not found with id: " + id
                        )
                );

        return categoryMapper.toResponse(category);
    }

    public List<CategoryResponse> getAllCategories(){

        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    public void deleteCategory(Long id){

        Category category = categoryRepository.findById(id)
                        .orElseThrow(() ->
                                new CategoryNotFoundException(
                                        "Category not found with id: " + id
                                )
                        );

        categoryRepository.delete(category);
    }
}
