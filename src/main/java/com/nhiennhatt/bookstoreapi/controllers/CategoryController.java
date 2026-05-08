package com.nhiennhatt.bookstoreapi.controllers;

import com.nhiennhatt.bookstoreapi.common.classes.CurrentUser;
import com.nhiennhatt.bookstoreapi.dto.BooleanDto;
import com.nhiennhatt.bookstoreapi.dto.category.CreateCategoryResponse;
import com.nhiennhatt.bookstoreapi.models.Category;
import com.nhiennhatt.bookstoreapi.services.CategoryService;
import com.nhiennhatt.bookstoreapi.validations.category.CreateCategoryValidation;
import com.nhiennhatt.bookstoreapi.validations.category.UpdateCategoryValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController()
@RequestMapping("/categories")
@Tag(name = "Categories", description = "The Categories API")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PreAuthorize("isAuthenticated() && hasRole('CONTENT_MANAGER')")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth")})
    @PostMapping("")
    public ResponseEntity<CreateCategoryResponse> createCategory(@Valid @RequestBody CreateCategoryValidation category) {
        CreateCategoryResponse rs = categoryService.createCategory(category);
        return ResponseEntity.status(201).body(rs);
    }

    @PreAuthorize("isAuthenticated() && hasRole('CONTENT_MANAGER')")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth")})
    @PostMapping(path = "/{id}/img", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Map<String, String>> uploadCategoryImage(
            @Valid @PathVariable UUID id, @Valid @RequestParam("file") MultipartFile file
    ) {
        categoryService.uploadCategoryImage(id, file);
        return ResponseEntity.ok(Map.of("message", "Image uploaded successfully"));
    }

    @GetMapping("/slug/{slug}")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth"), @SecurityRequirement(name = "")})
    public ResponseEntity<Category> getCategory(@PathVariable("slug") String slug, @AuthenticationPrincipal CurrentUser user) {
        return ResponseEntity.ok(categoryService.getCategoryBySlug(slug, user));
    }

    @GetMapping("/slug/{slug}/valid")
    public ResponseEntity<BooleanDto> isValidSlug(@PathVariable("slug") String slug) {
        return ResponseEntity.ok(new BooleanDto(categoryService.isValidSlug(slug)));
    }

    @GetMapping("/{id}")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth"), @SecurityRequirement(name = "")})
    public ResponseEntity<Category> getCategory(@PathVariable("id") UUID id, @AuthenticationPrincipal CurrentUser user) {
        return ResponseEntity.ok(categoryService.getCategoryById(id, user));
    }

    @GetMapping("")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth"), @SecurityRequirement(name = "")})
    public ResponseEntity<List<Category>> getAllCategories(
            @AuthenticationPrincipal CurrentUser user,
            @RequestParam(required = false) UUID cursor,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "10") @Min(1) @Max(30) int limit,
            @RequestParam(required = false) Boolean isPublic,
            @RequestParam(required = false) Boolean isFeatured
    ) {
        List<Category> categories = categoryService.getAllCategories(cursor, limit, isPublic, keyword, isFeatured, user);
        return ResponseEntity.ok(categories);
    }

    @PatchMapping("/{id}")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth")})
    @PreAuthorize("isAuthenticated() && hasRole('CONTENT_MANAGER')")
    public ResponseEntity<Void> updateCategory(@PathVariable("id") UUID id, @Valid @RequestBody UpdateCategoryValidation category) {
        categoryService.updateCategory(id, category);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(security = {@SecurityRequirement(name = "bearer-auth")})
    @PreAuthorize("isAuthenticated() && hasRole('CONTENT_MANAGER')")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") UUID id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
