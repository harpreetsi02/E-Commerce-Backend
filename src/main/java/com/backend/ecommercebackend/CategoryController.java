package com.backend.ecommercebackend;

import com.backend.ecommercebackend.dto.request.CategoryRequest;
import com.backend.ecommercebackend.dto.response.CategoryResponse;
import com.backend.ecommercebackend.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @Valid @RequestBody CategoryRequest request
    ) {

        CategoryResponse category =
                categoryService.createCategory(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(category);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> findCategoryById(
            @PathVariable Long id
    ) {

        CategoryResponse category =
                categoryService.getCategoryById(id);

        return ResponseEntity.ok(category);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategory(){

        List<CategoryResponse> categories =
                categoryService.getAllCategories();

        return ResponseEntity.ok(categories);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Long id
    ) {

        categoryService.deleteCategory(id);

        return ResponseEntity.noContent().build();
    }
}
