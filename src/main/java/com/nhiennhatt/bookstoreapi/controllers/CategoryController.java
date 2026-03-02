package com.nhiennhatt.bookstoreapi.controllers;

import com.nhiennhatt.bookstoreapi.common.classes.CurrentUser;
import com.nhiennhatt.bookstoreapi.dto.category.CreateCategoryResponse;
import com.nhiennhatt.bookstoreapi.models.Category;
import com.nhiennhatt.bookstoreapi.models.CustomUserDetails;
import com.nhiennhatt.bookstoreapi.models.User;
import com.nhiennhatt.bookstoreapi.services.CategoryService;
import com.nhiennhatt.bookstoreapi.validations.category.CategoriesFilter;
import com.nhiennhatt.bookstoreapi.validations.category.CreateCategoryValidation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

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

    @PreAuthorize("isAuthenticated() && hasRole('CONTENT_MANAGER')")
    @PutMapping(path = "/{slug}/img", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Map<String, String>> uploadCategoryImage(
            @Valid @PathVariable String slug, @Valid @RequestParam("file") MultipartFile file
    ) {
        categoryService.uploadCategoryImage(slug, file);
        return ResponseEntity.ok(Map.of("message", "Image uploaded successfully"));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<Category> getCategory(@PathVariable("slug") String slug, @AuthenticationPrincipal CurrentUser user) {
        return ResponseEntity.ok(categoryService.getCategoryBySlug(slug, user == null ? null : user));
    }

    @GetMapping("")
    public ResponseEntity<List<Category>> getAllCategories(@AuthenticationPrincipal CurrentUser user, @Valid @RequestBody CategoriesFilter filter) {
        List<Category> categories = categoryService.getAllCategories(filter.getCursor(),
                filter.getLimit(),
                filter.getIsPublic(),
                user
        );
        return ResponseEntity.ok(categories);
    }
}
