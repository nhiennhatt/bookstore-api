package com.nhiennhatt.bookstoreapi.controllers;

import com.nhiennhatt.bookstoreapi.dto.category.CreateCategoryResponse;
import com.nhiennhatt.bookstoreapi.models.Category;
import com.nhiennhatt.bookstoreapi.services.CategoryService;
import com.nhiennhatt.bookstoreapi.validations.category.CreateCategoryValidation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PreAuthorize("isAuthenticated() && hasRole('CONTENT_MANAGER')")
    @PostMapping("")
    public ResponseEntity<CreateCategoryResponse> createCategory(@Valid @RequestBody CreateCategoryValidation category) {
        CreateCategoryResponse rs = categoryService.createCategory(category);
        return ResponseEntity.status(201).body(rs);
    }
}
