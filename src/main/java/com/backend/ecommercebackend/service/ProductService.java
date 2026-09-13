package com.backend.ecommercebackend.service;

import com.backend.ecommercebackend.dto.request.ProductRequest;
import com.backend.ecommercebackend.dto.response.ProductResponse;
import com.backend.ecommercebackend.entity.Category;
import com.backend.ecommercebackend.entity.Product;
import com.backend.ecommercebackend.exception.CategoryNotFoundException;
import com.backend.ecommercebackend.exception.ProductNotFoundException;
import com.backend.ecommercebackend.mapper.ProductMapper;
import com.backend.ecommercebackend.repository.CategoryRepository;
import com.backend.ecommercebackend.repository.ProductRepository;
import com.backend.ecommercebackend.specification.ProductSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    public ProductService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            ProductMapper productMapper
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest request){

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() ->
                        new CategoryNotFoundException(
                                "Category not found with id: " + request.getCategoryId()
                        )
                );

        Product product = productMapper.toEntity(request, category);

        Product savedProduct = productRepository.save(product);

        return productMapper.toResponse(savedProduct);
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id){

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id: " + id
                        )
                );

        return productMapper.toResponse(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts(){

        return productRepository.getAllWithCategory()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Transactional
    public void deleteProduct(Long id){

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id: " + id
                        )
                );

        productRepository.delete(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> searchProduct(
            String name,
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable
    ) {
        Specification<Product> spec = Specification
                .where(ProductSpecification.hasName(name))
                .and(ProductSpecification.hasCategoryId(categoryId))
                .and(ProductSpecification.priceGreaterThanOrEqual(minPrice))
                .and(ProductSpecification.priceLessThanEqual(maxPrice));

        Page<Product> products = productRepository.findAll(spec, pageable);

        return products.map(productMapper::toResponse);
    }
}
